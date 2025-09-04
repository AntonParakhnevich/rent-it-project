import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { UserResponse } from '../../types';
import { userApi } from '../../services/api';
import './UserProfile.css';

const UserProfile: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();
  const [user, setUser] = useState<UserResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchUser = async () => {
      if (!userId) {
        setError('ID пользователя не указан');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const userData = await userApi.getById(parseInt(userId));
        setUser(userData);
      } catch (error: any) {
        setError(error.response?.data?.message || 'Ошибка загрузки профиля пользователя');
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [userId]);

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
        <p>Загрузка профиля...</p>
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

  if (!user) {
    return (
      <div className="error-container">
        <div className="error-message">
          <h2>Пользователь не найден</h2>
          <p>Пользователь с указанным ID не существует</p>
        </div>
      </div>
    );
  }

  const getRatingStars = (rating?: number) => {
    if (!rating) return '☆☆☆☆☆';
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;
    let stars = '★'.repeat(fullStars);
    if (hasHalfStar) stars += '☆';
    stars += '☆'.repeat(5 - fullStars - (hasHalfStar ? 1 : 0));
    return stars;
  };

  const getStatusBadge = () => {
    if (!user.enabled) {
      return <span className="status-badge status-disabled">Заблокирован</span>;
    }
    if (!user.verified) {
      return <span className="status-badge status-unverified">Не подтвержден</span>;
    }
    return <span className="status-badge status-verified">Подтвержден</span>;
  };

  return (
    <div className="user-profile">
      <div className="profile-header">
        <div className="profile-avatar">
          <div className="avatar-placeholder">
            {user.firstName[0]}{user.lastName[0]}
          </div>
        </div>
        
        <div className="profile-info">
          <h1>{user.firstName} {user.lastName}</h1>
          <p className="user-email">{user.email}</p>
          
          <div className="user-meta">
            {getStatusBadge()}
            
            <div className="user-rating">
              <span className="rating-stars">{getRatingStars(user.rating)}</span>
              <span className="rating-value">
                {user.rating ? user.rating.toFixed(1) : 'Нет рейтинга'}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div className="profile-content">
        <div className="profile-section">
          <h2>Контактная информация</h2>
          <div className="info-grid">
            <div className="info-item">
              <label>Email:</label>
              <span>{user.email}</span>
            </div>
            
            {user.phoneNumber && (
              <div className="info-item">
                <label>Телефон:</label>
                <span>{user.phoneNumber}</span>
              </div>
            )}
            
            <div className="info-item">
              <label>Статус:</label>
              <span>{user.enabled ? 'Активен' : 'Заблокирован'}</span>
            </div>
            
            <div className="info-item">
              <label>Подтверждение:</label>
              <span>{user.verified ? 'Подтвержден' : 'Не подтвержден'}</span>
            </div>
          </div>
        </div>

        {user.description && (
          <div className="profile-section">
            <h2>О пользователе</h2>
            <div className="user-description">
              <p>{user.description}</p>
            </div>
          </div>
        )}

        <div className="profile-section">
          <h2>Роли</h2>
          <div className="roles-list">
            {user.roles.map((role, index) => (
              <span key={index} className="role-badge">
                {role}
              </span>
            ))}
          </div>
        </div>

        <div className="profile-section">
          <h2>Статистика</h2>
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon">📊</div>
              <div className="stat-info">
                <span className="stat-value">{user.rating?.toFixed(1) || '0.0'}</span>
                <span className="stat-label">Рейтинг</span>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon">✅</div>
              <div className="stat-info">
                <span className="stat-value">{user.verified ? 'Да' : 'Нет'}</span>
                <span className="stat-label">Верификация</span>
              </div>
            </div>
            
            <div className="stat-card">
              <div className="stat-icon">🏠</div>
              <div className="stat-info">
                <span className="stat-value">0</span>
                <span className="stat-label">Активные аренды</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
