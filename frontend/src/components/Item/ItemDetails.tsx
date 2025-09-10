import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ItemResponse, UserResponse, UnavailableDatesResponse } from '../../types';
import { itemApi, userApi } from '../../services/api';
import { usePermissions } from '../../hooks/usePermissions';
import AvailabilityCalendar from './AvailabilityCalendar';
import './ItemDetails.css';

const ItemDetails: React.FC = () => {
  const { itemId } = useParams<{ itemId: string }>();
  const navigate = useNavigate();
  const { isRenter, canViewUsers } = usePermissions();

  const [item, setItem] = useState<ItemResponse | null>(null);
  const [owner, setOwner] = useState<UserResponse | null>(null);
  const [unavailableDates, setUnavailableDates] = useState<UnavailableDatesResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  const categories = {
    'ELECTRONICS': 'Электроника',
    'TOOLS': 'Инструменты',
    'SPORTS_EQUIPMENT': 'Спортивное снаряжение',
    'CAMPING_GEAR': 'Туристическое снаряжение',
    'PARTY_SUPPLIES': 'Товары для праздников',
    'MUSICAL_INSTRUMENTS': 'Музыкальные инструменты',
    'PHOTOGRAPHY_EQUIPMENT': 'Фото оборудование',
    'GARDENING_TOOLS': 'Садовые инструменты',
    'FURNITURE': 'Мебель',
    'CLOTHING': 'Одежда',
    'BOOKS': 'Книги',
    'OTHER': 'Другое'
  };

  useEffect(() => {
    const loadItemDetails = async () => {
      if (!itemId) {
        setError('ID товара не указан');
        setLoading(false);
        return;
      }

      console.log('Loading item details for itemId:', itemId);

      try {
        setLoading(true);
        setError(null);

        // Загружаем информацию о товаре
        console.log('Fetching item data...');
        const itemData = await itemApi.getById(parseInt(itemId));
        setItem(itemData);

        // Загружаем информацию о владельце, если есть права
        if (canViewUsers() && itemData.ownerId) {
          try {
            console.log('Fetching owner data...');
            const ownerData = await userApi.getById(itemData.ownerId);
            setOwner(ownerData);
          } catch (ownerError) {
            console.warn('Не удалось загрузить информацию о владельце:', ownerError);
          }
        }

        // Загружаем недоступные даты для календаря
        const startDate = new Date();
        const endDate = new Date();
        endDate.setFullYear(endDate.getFullYear() + 1); // На год вперед

        try {
          console.log('Fetching unavailable dates for item:', itemData.id);
          const unavailableData = await itemApi.getUnavailableDates(
            itemData.id,
            startDate.toISOString().split('T')[0],
            endDate.toISOString().split('T')[0]
          );
          console.log('Unavailable dates loaded:', unavailableData);
          setUnavailableDates(unavailableData);
        } catch (datesError) {
          console.warn('Не удалось загрузить недоступные даты:', datesError);
        }

      } catch (err) {
        console.error('Ошибка при загрузке товара:', err);
        setError('Не удалось загрузить информацию о товаре. Товар может не существовать.');
      } finally {
        setLoading(false);
      }
    };

    loadItemDetails();
  }, [itemId]); // Убираем canViewUsers из зависимостей

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('ru-RU', {
      style: 'currency',
      currency: 'BYN'
    }).format(price);
  };

  const nextImage = () => {
    if (item?.images && item.images.length > 1) {
      setCurrentImageIndex((prev) => (prev + 1) % item.images.length);
    }
  };

  const prevImage = () => {
    if (item?.images && item.images.length > 1) {
      setCurrentImageIndex((prev) => (prev - 1 + item.images.length) % item.images.length);
    }
  };

  if (!isRenter()) {
    return (
      <div className="item-details-container">
        <div className="access-denied">
          <h2>Доступ запрещен</h2>
          <p>Только пользователи с ролью RENTER могут просматривать детали товаров.</p>
          <button onClick={() => navigate('/dashboard')} className="back-btn">
            Вернуться на главную
          </button>
        </div>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="item-details-container">
        <div className="loading">
          <div className="spinner"></div>
          <p>Загрузка информации о товаре...</p>
        </div>
      </div>
    );
  }

  if (error || !item) {
    return (
      <div className="item-details-container">
        <div className="error-message">
          <h2>Ошибка</h2>
          <p>{error || 'Товар не найден'}</p>
          <button onClick={() => navigate('/search')} className="back-btn">
            Вернуться к поиску
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="item-details-container">
      <div className="item-details-header">
        <button onClick={() => navigate('/search')} className="back-btn">
          ← Вернуться к поиску
        </button>
      </div>

      <div className="item-details-content">
        {/* Изображения */}
        <div className="item-images-section">
          <div className="main-image">
            {item.images && item.images.length > 0 ? (
              <>
                <img 
                  src={item.images[currentImageIndex]} 
                  alt={item.title}
                  onError={(e) => {
                    (e.target as HTMLImageElement).src = '/placeholder-image.jpg';
                  }}
                />
                {item.images.length > 1 && (
                  <>
                    <button className="image-nav prev" onClick={prevImage}>
                      ‹
                    </button>
                    <button className="image-nav next" onClick={nextImage}>
                      ›
                    </button>
                  </>
                )}
              </>
            ) : (
              <div className="no-image">
                <span>Нет изображений</span>
              </div>
            )}
          </div>

          {item.images && item.images.length > 1 && (
            <div className="image-thumbnails">
              {item.images.map((image, index) => (
                <img
                  key={index}
                  src={image}
                  alt={`${item.title} ${index + 1}`}
                  className={index === currentImageIndex ? 'active' : ''}
                  onClick={() => setCurrentImageIndex(index)}
                  onError={(e) => {
                    (e.target as HTMLImageElement).src = '/placeholder-image.jpg';
                  }}
                />
              ))}
            </div>
          )}
        </div>

        {/* Основная информация */}
        <div className="item-main-info">
          <h1 className="item-title">{item.title}</h1>
          
          <div className="item-meta">
            <div className="item-category">
              {categories[item.category as keyof typeof categories] || item.category}
            </div>
            <div className="item-location">
              📍 {item.location}
            </div>
          </div>

          <div className="item-pricing">
            <div className="price-per-day">
              <span className="price">{formatPrice(item.pricePerDay)}</span>
              <span className="period">/день</span>
            </div>
            <div className="deposit">
              Залог: {formatPrice(item.depositAmount)}
            </div>
          </div>

          <div className="item-availability">
            <span className={`availability-status ${item.available ? 'available' : 'unavailable'}`}>
              {item.available ? '✓ Доступен для аренды' : '✗ Недоступен'}
            </span>
          </div>

          <div className="item-description">
            <h3>Описание</h3>
            <p>{item.description}</p>
          </div>

          {/* Информация о владельце */}
          {owner && (
            <div className="owner-info">
              <h3>Владелец</h3>
              <div className="owner-details">
                <div className="owner-name">
                  {owner.firstName} {owner.lastName}
                </div>
                {owner.phoneNumber && (
                  <div className="owner-phone">
                    📞 {owner.phoneNumber}
                  </div>
                )}
                {owner.rating && (
                  <div className="owner-rating">
                    ⭐ Рейтинг: {owner.rating.toFixed(1)}
                  </div>
                )}
                {owner.verified && (
                  <div className="owner-verified">
                    ✓ Верифицированный пользователь
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Календарь доступности */}
      {item.available && (
        <div className="availability-section">
          <h3>Календарь доступности</h3>
          <p className="calendar-description">
            Зеленые даты - свободны для аренды, красные - заняты
          </p>
          <AvailabilityCalendar 
            itemId={item.id}
            unavailableDates={unavailableDates?.unavailableDates || []}
          />
        </div>
      )}

      {/* Действия */}
      <div className="item-actions">
        {item.available ? (
          <div className="action-buttons">
            <button 
              className="rent-btn primary"
              onClick={() => {
                // TODO: Реализовать создание заявки на аренду
                alert('Функция создания заявки на аренду будет реализована позже');
              }}
            >
              Арендовать
            </button>
            <button 
              className="contact-btn secondary"
              onClick={() => {
                if (owner?.phoneNumber) {
                  window.open(`tel:${owner.phoneNumber}`);
                } else {
                  alert('Контактная информация владельца недоступна');
                }
              }}
            >
              Связаться с владельцем
            </button>
          </div>
        ) : (
          <div className="unavailable-notice">
            <p>Этот товар временно недоступен для аренды</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ItemDetails;
