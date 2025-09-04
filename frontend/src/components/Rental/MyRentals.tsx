import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { RentalResponse } from '../../types';
import { rentalApi } from '../../services/api';
import './MyRentals.css';

const MyRentals: React.FC = () => {
  const { user } = useAuth();
  const [rentals, setRentals] = useState<RentalResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filter, setFilter] = useState<string>('all');
  const [sortBy, setSortBy] = useState<'startDate' | 'endDate' | 'totalPrice'>('startDate');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('desc');

  useEffect(() => {
    const fetchRentals = async () => {
      if (!user) return;

      try {
        setLoading(true);
        setError(null);
        const userRentals = await rentalApi.getAllByUserId(user.userId);
        setRentals(userRentals);
      } catch (error: any) {
        setError(error.response?.data?.message || 'Ошибка загрузки аренд');
      } finally {
        setLoading(false);
      }
    };

    fetchRentals();
  }, [user]);

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
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

  const filteredAndSortedRentals = rentals
    .filter(rental => {
      if (filter === 'all') return true;
      return rental.status.toLowerCase() === filter.toLowerCase();
    })
    .sort((a, b) => {
      let aValue: any, bValue: any;
      
      switch (sortBy) {
        case 'startDate':
          aValue = new Date(a.startDate);
          bValue = new Date(b.startDate);
          break;
        case 'endDate':
          aValue = new Date(a.endDate);
          bValue = new Date(b.endDate);
          break;
        case 'totalPrice':
          aValue = a.totalPrice;
          bValue = b.totalPrice;
          break;
        default:
          return 0;
      }

      if (sortOrder === 'asc') {
        return aValue < bValue ? -1 : aValue > bValue ? 1 : 0;
      } else {
        return aValue > bValue ? -1 : aValue < bValue ? 1 : 0;
      }
    });

  const getStatistics = () => {
    const stats = {
      total: rentals.length,
      confirmed: rentals.filter(r => r.status === 'CONFIRMED').length,
      inProgress: rentals.filter(r => r.status === 'IN_PROGRESS').length,
      pending: rentals.filter(r => r.status === 'PENDING').length,
      completed: rentals.filter(r => r.status === 'COMPLETED').length,
      totalSpent: rentals.reduce((sum, r) => sum + r.totalPrice, 0)
    };
    return stats;
  };

  const stats = getStatistics();

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
        <p>Загрузка ваших аренд...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-message">
          <h2>Ошибка</h2>
          <p>{error}</p>
          <button 
            onClick={() => window.location.reload()} 
            className="btn btn-primary"
          >
            Попробовать снова
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="my-rentals">
      <div className="rentals-header">
        <h1>Мои аренды</h1>
        <p>Управляйте своими арендными договорами</p>
      </div>

      {/* Статистика */}
      <div className="rentals-stats">
        <div className="stat-card">
          <div className="stat-icon">📊</div>
          <div className="stat-info">
            <span className="stat-value">{stats.total}</span>
            <span className="stat-label">Всего аренд</span>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">🟢</div>
          <div className="stat-info">
            <span className="stat-value">{stats.confirmed}</span>
            <span className="stat-label">Подтвержденные</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">🟢</div>
          <div className="stat-info">
            <span className="stat-value">{stats.inProgress}</span>
            <span className="stat-label">В процессе</span>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">⏳</div>
          <div className="stat-info">
            <span className="stat-value">{stats.pending}</span>
            <span className="stat-label">В ожидании</span>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">✅</div>
          <div className="stat-info">
            <span className="stat-value">{stats.completed}</span>
            <span className="stat-label">Завершённые</span>
          </div>
        </div>
        
        <div className="stat-card total">
          <div className="stat-icon">💰</div>
          <div className="stat-info">
            <span className="stat-value">{formatPrice(stats.totalSpent)}</span>
            <span className="stat-label">Общая сумма</span>
          </div>
        </div>
      </div>

      {/* Фильтры и сортировка */}
      <div className="rentals-controls">
        <div className="filters">
          <label>Фильтр по статусу:</label>
          <select 
            value={filter} 
            onChange={(e) => setFilter(e.target.value)}
            className="filter-select"
          >
            <option value="all">Все аренды</option>
            <option value="pending">Ожидают</option>
            <option value="confirmed">Подтвержденные</option>
            <option value="inProgress">В процессе</option>
            <option value="completed">Завершённые</option>
            <option value="cancelled">Отменённые</option>
          </select>
        </div>

        <div className="sorting">
          <label>Сортировать по:</label>
          <select 
            value={sortBy} 
            onChange={(e) => setSortBy(e.target.value as any)}
            className="sort-select"
          >
            <option value="startDate">Дате начала</option>
            <option value="endDate">Дате окончания</option>
            <option value="totalPrice">Стоимости</option>
          </select>
          
          <button 
            onClick={() => setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')}
            className="sort-order-btn"
            title={sortOrder === 'asc' ? 'По возрастанию' : 'По убыванию'}
          >
            {sortOrder === 'asc' ? '↑' : '↓'}
          </button>
        </div>
      </div>

      {/* Список аренд */}
      {filteredAndSortedRentals.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">📦</div>
          <h3>
            {filter === 'all' ? 'У вас пока нет аренд' : `Нет аренд со статусом "${filter}"`}
          </h3>
          <p>
            {filter === 'all' 
              ? 'Когда вы начнёте арендовать предметы, они появятся здесь'
              : 'Попробуйте изменить фильтр для просмотра других аренд'
            }
          </p>
          {filter !== 'all' && (
            <button 
              onClick={() => setFilter('all')} 
              className="btn btn-secondary"
            >
              Показать все аренды
            </button>
          )}
        </div>
      ) : (
        <div className="rentals-grid">
          {filteredAndSortedRentals.map((rental) => (
            <div key={rental.id} className="rental-card">
              <div className="rental-card-header">
                <div className="rental-id">#{rental.id}</div>
                {getStatusBadge(rental.status)}
              </div>

              <div className="rental-card-body">
                <div className="rental-info">
                  <div className="info-row">
                    <span className="info-label">Предмет:</span>
                    <span className="info-value">#{rental.itemId}</span>
                  </div>
                  
                  <div className="info-row">
                    <span className="info-label">Период:</span>
                    <span className="info-value">
                      {formatDate(rental.startDate)} - {formatDate(rental.endDate)}
                    </span>
                  </div>
                  
                  <div className="info-row">
                    <span className="info-label">Стоимость:</span>
                    <span className="info-value price">
                      {formatPrice(rental.totalPrice)}
                    </span>
                  </div>
                  
                  {rental.depositAmount > 0 && (
                    <div className="info-row">
                      <span className="info-label">Залог:</span>
                      <span className="info-value">
                        {formatPrice(rental.depositAmount)}
                      </span>
                    </div>
                  )}
                </div>
              </div>

              <div className="rental-card-footer">
                <Link 
                  to={`/rental/${rental.id}`} 
                  className="btn btn-primary btn-sm"
                >
                  Подробнее
                </Link>
                
                {rental.status === 'PENDING' && (
                  <button 
                    className="btn btn-secondary btn-sm"
                    onClick={() => alert('Функция отмены в разработке')}
                  >
                    Отменить
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyRentals;
