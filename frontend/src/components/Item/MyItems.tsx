import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { ItemResponse, PageResponse } from '../../types';
import { itemApi } from '../../services/api';
import './MyItems.css';

const MyItems: React.FC = () => {
  const { user } = useAuth();
  const [items, setItems] = useState<ItemResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [filter, setFilter] = useState<string>('all');
  const [sortBy, setSortBy] = useState<'title' | 'pricePerDay' | 'category'>('title');
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

  useEffect(() => {
    const fetchItems = async () => {
      if (!user) return;

      try {
        setLoading(true);
        setError(null);
        const response = await itemApi.getByOwnerId(user.userId, currentPage, 10);
        setItems(response.content);
        setTotalPages(response.totalPages);
        setTotalElements(response.totalElements);
      } catch (error: any) {
        setError(error.response?.data?.message || 'Ошибка загрузки предметов');
      } finally {
        setLoading(false);
      }
    };

    fetchItems();
  }, [user, currentPage]);

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('ru-RU', {
      style: 'currency',
      currency: 'RUB'
    }).format(price);
  };

  const getAvailabilityBadge = (available: boolean) => {
    return (
      <span className={`availability-badge ${available ? 'available' : 'unavailable'}`}>
        {available ? 'Доступен' : 'Недоступен'}
      </span>
    );
  };

  const getCategoryIcon = (category: string) => {
    const categoryIcons: { [key: string]: string } = {
      'ELECTRONICS': '📱',
      'TOOLS': '🔧',
      'SPORTS': '⚽',
      'VEHICLES': '🚗',
      'BOOKS': '📚',
      'CLOTHING': '👕',
      'HOME': '🏠',
      'OTHER': '📦'
    };
    return categoryIcons[category] || '📦';
  };

  const filteredAndSortedItems = items
    .filter(item => {
      if (filter === 'all') return true;
      if (filter === 'available') return item.available;
      if (filter === 'unavailable') return !item.available;
      return item.category.toLowerCase() === filter.toLowerCase();
    })
    .sort((a, b) => {
      let aValue: any, bValue: any;
      
      switch (sortBy) {
        case 'title':
          aValue = a.title.toLowerCase();
          bValue = b.title.toLowerCase();
          break;
        case 'pricePerDay':
          aValue = a.pricePerDay;
          bValue = b.pricePerDay;
          break;
        case 'category':
          aValue = a.category;
          bValue = b.category;
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
      total: items.length,
      available: items.filter(item => item.available).length,
      unavailable: items.filter(item => !item.available).length,
      avgPrice: items.length > 0 ? items.reduce((sum, item) => sum + item.pricePerDay, 0) / items.length : 0
    };
    return stats;
  };

  const stats = getStatistics();

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
        <p>Загрузка ваших предметов...</p>
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
    <div className="my-items">
      <div className="items-header">
        <div className="header-content">
          <div>
            <h1>Мои предметы</h1>
            <p>Управляйте своими предметами для аренды</p>
          </div>
          <Link to="/items/create" className="btn btn-primary">
            <span className="btn-icon">➕</span>
            Добавить предмет
          </Link>
        </div>
      </div>

      {/* Статистика */}
      <div className="items-stats">
        <div className="stat-card">
          <div className="stat-icon">📊</div>
          <div className="stat-info">
            <span className="stat-value">{stats.total}</span>
            <span className="stat-label">Всего предметов</span>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">✅</div>
          <div className="stat-info">
            <span className="stat-value">{stats.available}</span>
            <span className="stat-label">Доступные</span>
          </div>
        </div>
        
        <div className="stat-card">
          <div className="stat-icon">❌</div>
          <div className="stat-info">
            <span className="stat-value">{stats.unavailable}</span>
            <span className="stat-label">Недоступные</span>
          </div>
        </div>
        
        <div className="stat-card total">
          <div className="stat-icon">💰</div>
          <div className="stat-info">
            <span className="stat-value">{formatPrice(stats.avgPrice)}</span>
            <span className="stat-label">Средняя цена/день</span>
          </div>
        </div>
      </div>

      {/* Фильтры и сортировка */}
      <div className="items-controls">
        <div className="filters">
          <label>Фильтр:</label>
          <select 
            value={filter} 
            onChange={(e) => setFilter(e.target.value)}
            className="filter-select"
          >
            <option value="all">Все предметы</option>
            <option value="available">Доступные</option>
            <option value="unavailable">Недоступные</option>
            <option value="electronics">Электроника</option>
            <option value="tools">Инструменты</option>
            <option value="sports">Спорт</option>
            <option value="vehicles">Транспорт</option>
            <option value="other">Другое</option>
          </select>
        </div>

        <div className="sorting">
          <label>Сортировать по:</label>
          <select 
            value={sortBy} 
            onChange={(e) => setSortBy(e.target.value as any)}
            className="sort-select"
          >
            <option value="title">Названию</option>
            <option value="pricePerDay">Цене</option>
            <option value="category">Категории</option>
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

      {/* Список предметов */}
      {filteredAndSortedItems.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">📦</div>
          <h3>
            {filter === 'all' ? 'У вас пока нет предметов' : `Нет предметов с фильтром "${filter}"`}
          </h3>
          <p>
            {filter === 'all' 
              ? 'Добавьте свои первые предметы для аренды'
              : 'Попробуйте изменить фильтр для просмотра других предметов'
            }
          </p>
          {filter === 'all' ? (
            <Link to="/items/create" className="btn btn-primary">
              Добавить первый предмет
            </Link>
          ) : (
            <button 
              onClick={() => setFilter('all')} 
              className="btn btn-secondary"
            >
              Показать все предметы
            </button>
          )}
        </div>
      ) : (
        <>
          <div className="items-grid">
            {filteredAndSortedItems.map((item) => (
              <div key={item.id} className="item-card">
                <div className="item-card-header">
                  <div className="item-category">
                    <span className="category-icon">{getCategoryIcon(item.category)}</span>
                    <span className="category-name">{item.category}</span>
                  </div>
                  {getAvailabilityBadge(item.available)}
                </div>

                <div className="item-card-body">
                  <div className="item-images">
                    {item.images && item.images.length > 0 ? (
                      <img 
                        src={Array.from(item.images)[0]} 
                        alt={item.title}
                        className="item-image"
                        onError={(e) => {
                          (e.target as HTMLImageElement).src = '/placeholder-image.jpg';
                        }}
                      />
                    ) : (
                      <div className="item-placeholder">
                        {getCategoryIcon(item.category)}
                      </div>
                    )}
                  </div>

                  <div className="item-info">
                    <h3 className="item-title">{item.title}</h3>
                    <p className="item-description">
                      {item.description.length > 100 
                        ? `${item.description.substring(0, 100)}...` 
                        : item.description
                      }
                    </p>
                    
                    <div className="item-details">
                      <div className="detail-row">
                        <span className="detail-label">Цена/день:</span>
                        <span className="detail-value price">
                          {formatPrice(item.pricePerDay)}
                        </span>
                      </div>
                      
                      {item.depositAmount > 0 && (
                        <div className="detail-row">
                          <span className="detail-label">Залог:</span>
                          <span className="detail-value">
                            {formatPrice(item.depositAmount)}
                          </span>
                        </div>
                      )}
                      
                      <div className="detail-row">
                        <span className="detail-label">Местоположение:</span>
                        <span className="detail-value">{item.location}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="item-card-footer">
                  <button 
                    className="btn btn-secondary btn-sm"
                    onClick={() => alert('Функция редактирования в разработке')}
                  >
                    Редактировать
                  </button>
                  
                  <button 
                    className="btn btn-primary btn-sm"
                    onClick={() => alert('Функция просмотра в разработке')}
                  >
                    Подробнее
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* Пагинация */}
          {totalPages > 1 && (
            <div className="pagination">
              <button 
                onClick={() => setCurrentPage(currentPage - 1)}
                disabled={currentPage === 0}
                className="pagination-btn"
              >
                ← Предыдущая
              </button>
              
              <span className="pagination-info">
                Страница {currentPage + 1} из {totalPages} 
                ({totalElements} предметов)
              </span>
              
              <button 
                onClick={() => setCurrentPage(currentPage + 1)}
                disabled={currentPage >= totalPages - 1}
                className="pagination-btn"
              >
                Следующая →
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default MyItems;
