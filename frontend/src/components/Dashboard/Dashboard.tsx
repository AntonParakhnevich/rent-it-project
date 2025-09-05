import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './Dashboard.css';

const Dashboard: React.FC = () => {
  const { user } = useAuth();

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>Добро пожаловать, {user?.firstName}!</h1>
        <p>Управляйте своими арендами и профилем</p>
      </div>

      <div className="dashboard-grid">
        <div className="dashboard-card">
          <div className="card-icon">👤</div>
          <h3>Профиль</h3>
          <p>Просмотр и редактирование информации о профиле</p>
          <Link to={`/profile/${user?.userId}`} className="btn btn-primary">
            Перейти к профилю
          </Link>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">🏠</div>
          <h3>Мои аренды</h3>
          <p>Управление активными и завершенными арендами</p>
          <div className="card-actions">
            <Link to="/my-rentals" className="btn btn-secondary">
              Просмотреть аренды
            </Link>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">📦</div>
          <h3>Мои предметы</h3>
          <p>Управление предметами для аренды</p>
          <div className="card-actions">
            <Link to="/my-items" className="btn btn-secondary">
              Мои предметы
            </Link>
            <Link to="/items/create" className="btn btn-primary">
              Добавить предмет
            </Link>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">📊</div>
          <h3>Статистика</h3>
          <p>Анализ доходов и активности аренды</p>
          <div className="stats-preview">
            <div className="stat-item">
              <span className="stat-value">0</span>
              <span className="stat-label">Активные аренды</span>
            </div>
            <div className="stat-item">
              <span className="stat-value">0₽</span>
              <span className="stat-label">Доход за месяц</span>
            </div>
          </div>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">⚙️</div>
          <h3>Настройки</h3>
          <p>Конфигурация аккаунта и уведомлений</p>
          <button className="btn btn-secondary" onClick={() => alert('Функция в разработке')}>
            Открыть настройки
          </button>
        </div>
      </div>

      <div className="quick-actions">
        <h2>Быстрые действия</h2>
        <div className="actions-grid">
          <Link to="/items/create" className="action-btn">
            <span className="action-icon">➕</span>
            Добавить новый предмет для аренды
          </Link>
          <Link to="/my-items" className="action-btn">
            <span className="action-icon">📦</span>
            Управление моими предметами
          </Link>
          <Link to="/my-rentals" className="action-btn">
            <span className="action-icon">🏠</span>
            Просмотреть мои аренды
          </Link>
          <button className="action-btn" onClick={() => alert('Функция в разработке')}>
            <span className="action-icon">💬</span>
            Сообщения
          </button>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
