import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

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
