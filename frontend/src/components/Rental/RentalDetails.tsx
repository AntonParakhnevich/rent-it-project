import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { usePermissions } from '../../hooks/usePermissions';
import { RentalResponse } from '../../types';
import { rentalApi } from '../../services/api';
import './RentalDetails.css';

const RentalDetails: React.FC = () => {
  const { rentalId } = useParams<{ rentalId: string }>();
  const { user } = useAuth();
  const { canConfirmRentals } = usePermissions();
  const [rental, setRental] = useState<RentalResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [confirm, setConfirm] = useState(false);
  const [cancelling, setCancelling] = useState(false);

  useEffect(() => {
    const fetchRental = async () => {
      if (!rentalId) {
        setError('ID аренды не указан');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const rentalData = await rentalApi.getById(parseInt(rentalId));
        setRental(rentalData);
      } catch (error: any) {
        setError(error.response?.data?.message || 'Ошибка загрузки данных аренды');
      } finally {
        setLoading(false);
      }
    };

    fetchRental();
  }, [rentalId]);

  const handleConfirm = async () => {
    if (!rental || !canConfirmRentals()) {
      alert('У вас нет прав для подтверждения аренды');
      return;
    }

    try {
      setConfirm(true);
      await rentalApi.confirm(rental.id);

      // Обновляем статус локально
      setRental({
        ...rental,
        status: 'CONFIRMED'
      });
      
      alert('Аренда успешно подтверждена!');
    } catch (error: any) {
      alert(error.response?.data?.message || 'Ошибка подтверждения аренды');
    } finally {
      setConfirm(false);
    }
  };

  const handleCancel = async () => {
    if (!rental) return;

    if (!window.confirm('Вы уверены, что хотите отменить эту аренду?')) {
      return;
    }

    try {
      setCancelling(true);
      await rentalApi.cancel(rental.id);

      // Обновляем статус локально
      setRental({
        ...rental,
        status: 'CANCELLED'
      });
      
      alert('Аренда успешно отменена!');
    } catch (error: any) {
      alert(error.response?.data?.message || 'Ошибка отмены аренды');
    } finally {
      setCancelling(false);
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('ru-RU', {
      style: 'currency',
      currency: 'RUB'
    }).format(price);
  };

  const getStatusBadge = (status: string) => {
    const statusConfig = {
      'PENDING': { label: 'Ожидает', className: 'status-pending' },
      'CONFIRMED': { label: 'Подтверждена', className: 'status-confirmed' },
      'IN_PROGRESS': { label: 'В процессе', className: 'status-in-progress' },
      'COMPLETED': { label: 'Завершена', className: 'status-completed' },
      'CANCELLED': { label: 'Отменена', className: 'status-cancelled' },
    };

    const config = statusConfig[status as keyof typeof statusConfig] || 
                  { label: status, className: 'status-unknown' };

    return (
      <span className={`status-badge ${config.className}`}>
        {config.label}
      </span>
    );
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
        <p>Загрузка данных аренды...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-message">
          <h2>Ошибка</h2>
          <p>{error}</p>
        </div>
      </div>
    );
  }

  if (!rental) {
    return (
      <div className="error-container">
        <div className="error-message">
          <h2>Аренда не найдена</h2>
          <p>Аренда с указанным ID не существует</p>
        </div>
      </div>
    );
  }

  return (
    <div className="rental-details">
      <div className="rental-header">
        <div className="rental-title">
          <h1>Аренда #{rental.id}</h1>
          {getStatusBadge(rental.status)}
        </div>
        
        <div className="rental-actions">
          {rental.status === 'PENDING' && canConfirmRentals() && (
            <button
              onClick={handleConfirm}
              disabled={confirm}
              className="btn btn-primary"
            >
              {confirm ? (
                <>
                  <div className="spinner small"></div>
                  Подтверждаю...
                </>
              ) : (
                <>
                  <span>✓</span>
                  Подтвердить аренду
                </>
              )}
            </button>
          )}

          {/* Кнопка отмены */}
          {(rental.status === 'PENDING' || rental.status === 'CONFIRMED') && (
            <button
              onClick={handleCancel}
              disabled={cancelling}
              className="btn btn-danger"
            >
              {cancelling ? (
                <>
                  <span className="spinner"></span>
                  Отменяю...
                </>
              ) : (
                <>
                  <span>✗</span>
                  Отменить аренду
                </>
              )}
            </button>
          )}
          
          {rental.status === 'PENDING' && !canConfirmRentals() && (
            <div className="status-info">
              <span className="info-icon">ℹ️</span>
              Аренда ожидает подтверждения арендодателя
            </div>
          )}
        </div>
      </div>

      <div className="rental-content">
        <div className="rental-section">
          <h2>Основная информация</h2>
          <div className="info-grid">
            <div className="info-item">
              <label>ID аренды:</label>
              <span>#{rental.id}</span>
            </div>
            
            <div className="info-item">
              <label>ID предмета:</label>
              <span>#{rental.itemId}</span>
            </div>
            
            <div className="info-item">
              <label>ID арендатора:</label>
              <span>#{rental.renterId}</span>
            </div>
            
            <div className="info-item">
              <label>Статус:</label>
              {getStatusBadge(rental.status)}
            </div>
          </div>
        </div>

        <div className="rental-section">
          <h2>Период аренды</h2>
          <div className="date-range">
            <div className="date-item">
              <div className="date-icon">📅</div>
              <div className="date-info">
                <label>Начало аренды:</label>
                <span>{formatDate(rental.startDate)}</span>
              </div>
            </div>
            
            <div className="date-separator">→</div>
            
            <div className="date-item">
              <div className="date-icon">📅</div>
              <div className="date-info">
                <label>Окончание аренды:</label>
                <span>{formatDate(rental.endDate)}</span>
              </div>
            </div>
          </div>
        </div>

        <div className="rental-section">
          <h2>Финансовая информация</h2>
          <div className="price-grid">
            <div className="price-card">
              <div className="price-icon">💰</div>
              <div className="price-info">
                <label>Общая стоимость:</label>
                <span className="price-value">{formatPrice(rental.totalPrice)}</span>
              </div>
            </div>
            
            <div className="price-card">
              <div className="price-icon">🛡️</div>
              <div className="price-info">
                <label>Залог:</label>
                <span className="price-value">{formatPrice(rental.depositAmount)}</span>
              </div>
            </div>
            
            <div className="price-card total">
              <div className="price-icon">💳</div>
              <div className="price-info">
                <label>К оплате:</label>
                <span className="price-value total-value">
                  {formatPrice(rental.totalPrice + rental.depositAmount)}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="rental-section">
          <h2>Действия</h2>
          <div className="actions-grid">
            {canConfirmRentals() && (
              <button 
                className="action-btn"
                onClick={() => alert('Функция в разработке')}
              >
                <span className="action-icon">👤</span>
                Просмотреть арендатора
              </button>
            )}
            
            <button 
              className="action-btn"
              onClick={() => alert('Функция в разработке')}
            >
              <span className="action-icon">📦</span>
              Детали предмета
            </button>
            
            <button 
              className="action-btn"
              onClick={() => alert('Функция в разработке')}
            >
              <span className="action-icon">💬</span>
              {canConfirmRentals() ? 'Связаться с арендатором' : 'Связаться с арендодателем'}
            </button>
            
            {rental.status === 'CONFIRMED' && canConfirmRentals() && (
              <button 
                className="action-btn danger"
                onClick={() => alert('Функция в разработке')}
              >
                <span className="action-icon">❌</span>
                Завершить аренду
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default RentalDetails;
