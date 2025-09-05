import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { ItemRequest } from '../../types';
import { itemApi } from '../../services/api';
import './CreateItem.css';

const CreateItem: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState<ItemRequest>({
    title: '',
    description: '',
    pricePerDay: 0,
    depositAmount: 0,
    category: 'OTHER',
    images: [],
    available: true,
    location: '',
    ownerId: user?.userId || 0
  });
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [imageUrls, setImageUrls] = useState<string>('');

  const categories = [
    { value: 'ELECTRONICS', label: 'Электроника', icon: '📱' },
    { value: 'TOOLS', label: 'Инструменты', icon: '🔧' },
    { value: 'SPORTS', label: 'Спорт и отдых', icon: '⚽' },
    { value: 'VEHICLES', label: 'Транспорт', icon: '🚗' },
    { value: 'BOOKS', label: 'Книги', icon: '📚' },
    { value: 'CLOTHING', label: 'Одежда', icon: '👕' },
    { value: 'HOME', label: 'Дом и быт', icon: '🏠' },
    { value: 'OTHER', label: 'Другое', icon: '📦' }
  ];

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    
    setFormData(prev => ({
      ...prev,
      [name]: type === 'number' ? parseFloat(value) || 0 : 
              type === 'checkbox' ? (e.target as HTMLInputElement).checked : 
              value
    }));
  };

  const handleImageUrlsChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const urls = e.target.value;
    setImageUrls(urls);
    
    // Парсим URLs (по строкам или через запятую)
    const urlArray = urls
      .split(/[\n,]/)
      .map(url => url.trim())
      .filter(url => url.length > 0);
    
    setFormData(prev => ({
      ...prev,
      images: urlArray
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Валидация
    if (!formData.title.trim()) {
      setError('Пожалуйста, введите название предмета');
      return;
    }

    if (!formData.description.trim()) {
      setError('Пожалуйста, введите описание предмета');
      return;
    }

    if (formData.pricePerDay <= 0) {
      setError('Цена за день должна быть больше 0');
      return;
    }

    if (!formData.location.trim()) {
      setError('Пожалуйста, укажите местоположение');
      return;
    }

    if (!user) {
      setError('Необходимо войти в систему');
      return;
    }

    try {
      setLoading(true);
      
      const itemData: ItemRequest = {
        ...formData,
        ownerId: user.userId
      };
      
      await itemApi.create(itemData);
      
      // Перенаправляем на страницу "Мои предметы"
      navigate('/my-items');
    } catch (error: any) {
      setError(error.response?.data?.message || 'Ошибка создания предмета');
    } finally {
      setLoading(false);
    }
  };

  const selectedCategory = categories.find(cat => cat.value === formData.category);

  return (
    <div className="create-item">
      <div className="create-item-header">
        <h1>Добавить новый предмет</h1>
        <p>Заполните информацию о предмете для аренды</p>
      </div>

      <form onSubmit={handleSubmit} className="create-item-form">
        <div className="form-section">
          <h2>Основная информация</h2>
          
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="title" className="form-label">
                Название предмета *
              </label>
              <input
                type="text"
                id="title"
                name="title"
                className="form-input"
                value={formData.title}
                onChange={handleInputChange}
                placeholder="Например: iPhone 15 Pro"
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="category" className="form-label">
                Категория *
              </label>
              <div className="category-select-wrapper">
                <span className="category-icon">{selectedCategory?.icon}</span>
                <select
                  id="category"
                  name="category"
                  className="form-input category-select"
                  value={formData.category}
                  onChange={handleInputChange}
                  disabled={loading}
                  required
                >
                  {categories.map(category => (
                    <option key={category.value} value={category.value}>
                      {category.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="description" className="form-label">
              Описание *
            </label>
            <textarea
              id="description"
              name="description"
              className="form-input form-textarea"
              value={formData.description}
              onChange={handleInputChange}
              placeholder="Подробное описание предмета, его состояние, особенности использования..."
              disabled={loading}
              rows={4}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="location" className="form-label">
              Местоположение *
            </label>
            <input
              type="text"
              id="location"
              name="location"
              className="form-input"
              value={formData.location}
              onChange={handleInputChange}
              placeholder="Город, район или адрес"
              disabled={loading}
              required
            />
          </div>
        </div>

        <div className="form-section">
          <h2>Цены и условия</h2>
          
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="pricePerDay" className="form-label">
                Цена за день (₽) *
              </label>
              <input
                type="number"
                id="pricePerDay"
                name="pricePerDay"
                className="form-input"
                value={formData.pricePerDay}
                onChange={handleInputChange}
                placeholder="1000"
                min="1"
                step="0.01"
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="depositAmount" className="form-label">
                Залог (₽)
              </label>
              <input
                type="number"
                id="depositAmount"
                name="depositAmount"
                className="form-input"
                value={formData.depositAmount}
                onChange={handleInputChange}
                placeholder="5000"
                min="0"
                step="0.01"
                disabled={loading}
              />
              <small className="form-hint">
                Залог возвращается после окончания аренды
              </small>
            </div>
          </div>

          <div className="form-group">
            <label className="checkbox-label">
              <input
                type="checkbox"
                name="available"
                checked={formData.available}
                onChange={handleInputChange}
                disabled={loading}
              />
              <span className="checkbox-custom"></span>
              Предмет доступен для аренды
            </label>
          </div>
        </div>

        <div className="form-section">
          <h2>Изображения</h2>
          
          <div className="form-group">
            <label htmlFor="images" className="form-label">
              URL изображений
            </label>
            <textarea
              id="images"
              className="form-input form-textarea"
              value={imageUrls}
              onChange={handleImageUrlsChange}
              placeholder="Вставьте ссылки на изображения (по одной на строку или через запятую)"
              disabled={loading}
              rows={3}
            />
            <small className="form-hint">
              Добавьте ссылки на изображения вашего предмета для привлечения арендаторов
            </small>
          </div>

          {formData.images.length > 0 && (
            <div className="image-preview">
              <h4>Предварительный просмотр:</h4>
              <div className="image-grid">
                {formData.images.map((url, index) => (
                  <div key={index} className="image-preview-item">
                    <img 
                      src={url} 
                      alt={`Изображение ${index + 1}`}
                      onError={(e) => {
                        (e.target as HTMLImageElement).style.display = 'none';
                      }}
                    />
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <div className="form-actions">
          <button
            type="button"
            onClick={() => navigate('/my-items')}
            className="btn btn-secondary"
            disabled={loading}
          >
            Отмена
          </button>
          
          <button
            type="submit"
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? (
              <>
                <div className="spinner small"></div>
                Создание...
              </>
            ) : (
              'Создать предмет'
            )}
          </button>
        </div>
      </form>
    </div>
  );
};

export default CreateItem;
