import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './Auth.css';

const Register: React.FC = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
    description: '',
    role: 'RENTER', // По умолчанию арендатор
    unp: '',
  });
  const [localError, setLocalError] = useState('');
  
  const { register, user, isLoading, error } = useAuth();
  const navigate = useNavigate();

  // Перенаправляем на дашборд, если пользователь уже авторизован
  useEffect(() => {
    if (user) {
      navigate('/dashboard');
    }
  }, [user, navigate]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLocalError('');

    // Валидация
    if (!formData.email || !formData.password || !formData.firstName || !formData.lastName) {
      setLocalError('Пожалуйста, заполните все обязательные поля');
      return;
    }

    if (!formData.email.includes('@')) {
      setLocalError('Пожалуйста, введите корректный email');
      return;
    }

    if (formData.password.length < 6) {
      setLocalError('Пароль должен содержать минимум 6 символов');
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      setLocalError('Пароли не совпадают');
      return;
    }

    // Валидация УНП для арендодателей
    if (formData.role === 'LANDLORD' && !formData.unp.trim()) {
      setLocalError('УНП обязателен для арендодателей');
      return;
    }

    if (formData.role === 'LANDLORD' && formData.unp.trim().length !== 9) {
      setLocalError('УНП должен содержать 9 цифр');
      return;
    }

    try {
      await register({
        email: formData.email,
        password: formData.password,
        firstName: formData.firstName,
        lastName: formData.lastName,
        phoneNumber: formData.phoneNumber || undefined,
        description: formData.description || undefined,
        unp: formData.role === 'LANDLORD' ? formData.unp : undefined,
        roles: [formData.role],
      });
      navigate('/dashboard');
    } catch (error) {
      // Ошибка уже обработана в AuthContext
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>Регистрация в Rent-It</h1>
          <p>Создайте новый аккаунт</p>
        </div>

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="firstName" className="form-label">
                Имя *
              </label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                className="form-input"
                value={formData.firstName}
                onChange={handleChange}
                placeholder="Ваше имя"
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label htmlFor="lastName" className="form-label">
                Фамилия *
              </label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                className="form-input"
                value={formData.lastName}
                onChange={handleChange}
                placeholder="Ваша фамилия"
                disabled={isLoading}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="email" className="form-label">
              Email *
            </label>
            <input
              type="email"
              id="email"
              name="email"
              className="form-input"
              value={formData.email}
              onChange={handleChange}
              placeholder="example@email.com"
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="phoneNumber" className="form-label">
              Телефон
            </label>
            <input
              type="tel"
              id="phoneNumber"
              name="phoneNumber"
              className="form-input"
              value={formData.phoneNumber}
              onChange={handleChange}
              placeholder="+7 (999) 123-45-67"
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="role" className="form-label">
              Роль *
            </label>
            <select
              id="role"
              name="role"
              className="form-input"
              value={formData.role}
              onChange={handleChange}
              disabled={isLoading}
            >
              <option value="RENTER">Арендатор</option>
              <option value="LANDLORD">Арендодатель</option>
            </select>
          </div>

          {formData.role === 'LANDLORD' && (
            <div className="form-group">
              <label htmlFor="unp" className="form-label">
                УНП (Учетный номер плательщика) *
              </label>
              <input
                type="text"
                id="unp"
                name="unp"
                className="form-input"
                value={formData.unp}
                onChange={handleChange}
                placeholder="123456789"
                disabled={isLoading}
                maxLength={9}
                pattern="[0-9]{9}"
              />
              <small className="form-hint">
                9-значный номер для арендодателей
              </small>
            </div>
          )}

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="password" className="form-label">
                Пароль *
              </label>
              <input
                type="password"
                id="password"
                name="password"
                className="form-input"
                value={formData.password}
                onChange={handleChange}
                placeholder="Минимум 6 символов"
                disabled={isLoading}
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword" className="form-label">
                Подтвердите пароль *
              </label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                className="form-input"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Повторите пароль"
                disabled={isLoading}
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="description" className="form-label">
              О себе
            </label>
            <textarea
              id="description"
              name="description"
              className="form-input form-textarea"
              value={formData.description}
              onChange={handleChange}
              placeholder="Расскажите немного о себе (необязательно)"
              disabled={isLoading}
              rows={3}
            />
          </div>

          {(localError || error) && (
            <div className="error-message">
              {localError || error}
            </div>
          )}

          <button
            type="submit"
            className="btn btn-primary auth-button"
            disabled={isLoading}
          >
            {isLoading ? (
              <div className="spinner"></div>
            ) : (
              'Зарегистрироваться'
            )}
          </button>
        </form>

        <div className="auth-footer">
          <p>
            Уже есть аккаунт?{' '}
            <Link to="/login" className="auth-link">
              Войти
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
