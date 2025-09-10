import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { usePermissions } from '../../hooks/usePermissions';
import { ProtectedComponent, LandlordOnly, RenterOnly } from '../Auth/ProtectedComponent';
import './Dashboard.css';

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const { isLandlord, canCreateItems, userRoles } = usePermissions();

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>Добро пожаловать, {user?.firstName}!</h1>
        <p>Управляйте своими арендами и профилем</p>
        {userRoles.length > 0 && (
          <div className="user-roles">
            <span className="role-label">Роль: </span>
            {userRoles.map(role => (
              <span key={role} className={`role-badge role-${role.toLowerCase()}`}>
                {role === 'LANDLORD' ? 'Арендодатель' : 
                 role === 'RENTER' ? 'Арендатор' : 
                 role === 'ADMIN' ? 'Администратор' : role}
              </span>
            ))}
          </div>
        )}
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

        <RenterOnly>
          <div className="dashboard-card">
            <div className="card-icon">🔍</div>
            <h3>Поиск товаров</h3>
            <p>Найти товары для аренды с фильтрами по категории, цене и местоположению</p>
            <div className="card-actions">
              <Link to="/search" className="btn btn-primary">
                Найти товары
              </Link>
            </div>
          </div>
        </RenterOnly>

        <LandlordOnly>
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
        </LandlordOnly>

        <div className="dashboard-card">
          <div className="card-icon">📊</div>
          <h3>Статистика</h3>
          <LandlordOnly fallback={<p>Анализ ваших арендованных предметов</p>}>
            <p>Анализ доходов и активности аренды</p>
          </LandlordOnly>
          <div className="stats-preview">
            <div className="stat-item">
              <span className="stat-value">0</span>
              <span className="stat-label">Активные аренды</span>
            </div>
            <LandlordOnly fallback={
              <div className="stat-item">
                <span className="stat-value">0₽</span>
                <span className="stat-label">Потрачено за месяц</span>
              </div>
            }>
              <div className="stat-item">
                <span className="stat-value">0₽</span>
                <span className="stat-label">Доход за месяц</span>
              </div>
            </LandlordOnly>
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
          <RenterOnly>
            <Link to="/search" className="action-btn">
              <span className="action-icon">🔍</span>
              Найти товары для аренды
            </Link>
          </RenterOnly>
          <LandlordOnly>
            <Link to="/items/create" className="action-btn">
              <span className="action-icon">➕</span>
              Добавить новый предмет для аренды
            </Link>
          </LandlordOnly>
          <LandlordOnly>
            <Link to="/my-items" className="action-btn">
              <span className="action-icon">📦</span>
              Управление моими предметами
            </Link>
          </LandlordOnly>
          <ProtectedComponent requiredRoles={['LANDLORD', 'RENTER', 'ADMIN']}>
            <Link to="/my-rentals" className="action-btn">
              <span className="action-icon">🏠</span>
              Просмотреть мои аренды
            </Link>
          </ProtectedComponent>
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
