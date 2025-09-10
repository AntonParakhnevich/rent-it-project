import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { ProtectedComponent, LandlordOnly, RenterOnly } from '../Auth/ProtectedComponent';

const Header: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="container">
        <div className="header-content">
          <Link to="/dashboard" className="logo">
            <h1>Rent-It</h1>
          </Link>

          <nav className="nav">
            <Link to="/dashboard" className="nav-link">
              Дашборд
            </Link>
            <RenterOnly>
              <Link to="/search" className="nav-link">
                Поиск товаров
              </Link>
            </RenterOnly>
            <ProtectedComponent requiredRoles={['LANDLORD', 'RENTER', 'ADMIN']}>
              <Link to="/my-rentals" className="nav-link">
                Мои аренды
              </Link>
            </ProtectedComponent>
            <LandlordOnly>
              <Link to="/my-items" className="nav-link">
                Мои предметы
              </Link>
            </LandlordOnly>
            {user && (
              <Link to={`/profile/${user.userId}`} className="nav-link">
                Профиль
              </Link>
            )}
          </nav>

          <div className="user-menu">
            {user && (
              <div className="user-info">
                <span className="user-name">
                  {user.firstName} {user.lastName}
                </span>
                <button onClick={handleLogout} className="btn btn-secondary logout-btn">
                  Выйти
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
