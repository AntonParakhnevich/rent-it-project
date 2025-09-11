import React, { useState, useEffect, useCallback, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { ItemResponse, ItemSearchFilters } from '../../types';
import { itemApi } from '../../services/api';
import { usePermissions } from '../../hooks/usePermissions';
import './ItemSearch.css';

const ItemSearch: React.FC = () => {
  const navigate = useNavigate();
  const { isRenter } = usePermissions();
  
  const [items, setItems] = useState<ItemResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [filters, setFilters] = useState<ItemSearchFilters>({
    category: '',
    maxPrice: undefined,
    location: '',
    page: 0,
    size: 20
  });

  const categories = [
    { value: '', label: 'Все категории' },
    { value: 'ELECTRONICS', label: 'Электроника' },
    { value: 'TOOLS', label: 'Инструменты' },
    { value: 'SPORTS_EQUIPMENT', label: 'Спортивное снаряжение' },
    { value: 'CAMPING_GEAR', label: 'Туристическое снаряжение' },
    { value: 'PARTY_SUPPLIES', label: 'Товары для праздников' },
    { value: 'MUSICAL_INSTRUMENTS', label: 'Музыкальные инструменты' },
    { value: 'PHOTOGRAPHY_EQUIPMENT', label: 'Фото оборудование' },
    { value: 'GARDENING_TOOLS', label: 'Садовые инструменты' },
    { value: 'FURNITURE', label: 'Мебель' },
    { value: 'CLOTHING', label: 'Одежда' },
    { value: 'BOOKS', label: 'Книги' },
    { value: 'OTHER', label: 'Другое' }
  ];

  const loadItems = useCallback(async (searchFilters: ItemSearchFilters) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await itemApi.getAvailable(searchFilters.page || 0, searchFilters.size || 20);
      
      setItems(response.content);
      setCurrentPage(response.number);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      console.error('Ошибка при загрузке товаров:', err);
      setError('Не удалось загрузить товары. Попробуйте позже.');
    } finally {
      setLoading(false);
    }
  }, []);

  const searchItems = useCallback(async (searchFilters: ItemSearchFilters) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await itemApi.search(searchFilters);
      
      setItems(response.content);
      setCurrentPage(response.number);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err) {
      console.error('Ошибка при поиске товаров:', err);
      setError('Не удалось найти товары. Попробуйте позже.');
    } finally {
      setLoading(false);
    }
  }, []);

  // Используем useRef для отслеживания выполненных запросов
  const hasLoadedInitialData = useRef(false);

  useEffect(() => {
    if (isRenter() && !hasLoadedInitialData.current) {
      // Загружаем товары только при первой загрузке компонента
      console.log('Initial load without filters');
      loadItems({ page: 0, size: 20 });
      hasLoadedInitialData.current = true;
    }
  }, [isRenter, loadItems]);

  const handleFilterChange = (field: keyof ItemSearchFilters, value: any) => {
    const newFilters = { 
      ...filters, 
      [field]: value === '' ? undefined : value,
      page: 0 // Сбрасываем страницу при изменении фильтров
    };
    setFilters(newFilters);
    
    console.log('Filter changed:', { field, value, newFilters, hasFilters: hasActiveFilters(newFilters) });
    
    if (hasActiveFilters(newFilters)) {
      console.log('Using searchItems with filters:', newFilters);
      searchItems(newFilters);
    } else {
      console.log('Using loadItems (no active filters):', newFilters);
      loadItems(newFilters);
    }
  };

  const hasActiveFilters = (searchFilters: ItemSearchFilters): boolean => {
    return !!(
      (searchFilters.category && searchFilters.category !== '') || 
      searchFilters.maxPrice || 
      (searchFilters.location && searchFilters.location !== '')
    );
  };

  const handlePageChange = (newPage: number) => {
    const newFilters = { ...filters, page: newPage };
    setFilters(newFilters);
    
    console.log('Page changed:', { newPage, newFilters, hasFilters: hasActiveFilters(newFilters) });
    
    if (hasActiveFilters(newFilters)) {
      console.log('Using searchItems for pagination with filters:', newFilters);
      searchItems(newFilters);
    } else {
      console.log('Using loadItems for pagination without filters:', newFilters);
      loadItems(newFilters);
    }
  };

  const clearFilters = () => {
    const clearedFilters = { ...filters, category: '', maxPrice: undefined, location: '', page: 0 };
    setFilters(clearedFilters);
    console.log('Filters cleared, loading items without filters:', clearedFilters);
    loadItems(clearedFilters);
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('ru-RU', {
      style: 'currency',
      currency: 'BYN'
    }).format(price);
  };

  if (!isRenter()) {
    return (
      <div className="item-search-container">
        <div className="access-denied">
          <h2>Доступ запрещен</h2>
          <p>Только пользователи с ролью RENTER могут просматривать товары для аренды.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="item-search-container">
      <div className="search-header">
        <h1>Поиск товаров для аренды</h1>
        <p className="search-subtitle">Найдите нужные товары для аренды</p>
      </div>

      {/* Фильтры */}
      <div className="search-filters">
        <div className="filter-row">
          <div className="filter-group">
            <label htmlFor="category">Категория:</label>
            <select
              id="category"
              value={filters.category || ''}
              onChange={(e) => handleFilterChange('category', e.target.value)}
            >
              {categories.map(cat => (
                <option key={cat.value} value={cat.value}>
                  {cat.label}
                </option>
              ))}
            </select>
          </div>

          <div className="filter-group">
            <label htmlFor="maxPrice">Максимальная цена (BYN/день):</label>
            <input
              type="number"
              id="maxPrice"
              min="0"
              step="0.01"
              placeholder="Введите цену"
              value={filters.maxPrice || ''}
              onChange={(e) => handleFilterChange('maxPrice', e.target.value ? parseFloat(e.target.value) : undefined)}
            />
          </div>

          <div className="filter-group">
            <label htmlFor="location">Местоположение:</label>
            <input
              type="text"
              id="location"
              placeholder="Введите город или адрес"
              value={filters.location || ''}
              onChange={(e) => handleFilterChange('location', e.target.value)}
            />
          </div>

          <button 
            type="button" 
            className="clear-filters-btn"
            onClick={clearFilters}
            disabled={!hasActiveFilters(filters)}
          >
            Очистить фильтры
          </button>
        </div>
      </div>

      {/* Результаты поиска */}
      <div className="search-results">
        <div className="results-header">
          <p className="results-count">
            Найдено товаров: {totalElements}
          </p>
        </div>

        {loading && (
          <div className="loading">
            <div className="spinner"></div>
            <p>Загрузка товаров...</p>
          </div>
        )}

        {error && (
          <div className="error-message">
            <p>{error}</p>
          </div>
        )}

        {!loading && !error && items.length === 0 && (
          <div className="no-results">
            <p>Товары не найдены. Попробуйте изменить критерии поиска.</p>
          </div>
        )}

        {!loading && !error && items.length > 0 && (
          <>
            <div className="items-grid">
              {items.map(item => (
                <div key={item.id} className="item-card">
                  <div className="item-images">
                    {item.images && item.images.length > 0 ? (
                      <img 
                        src={item.images[0]} 
                        alt={item.title}
                        onError={(e) => {
                          (e.target as HTMLImageElement).src = '/placeholder-image.jpg';
                        }}
                      />
                    ) : (
                      <div className="no-image">
                        <span>Нет фото</span>
                      </div>
                    )}
                  </div>

                  <div className="item-info">
                    <h3 className="item-title">{item.title}</h3>
                    <p className="item-description">
                      {item.description.length > 100 
                        ? `${item.description.substring(0, 100)}...` 
                        : item.description}
                    </p>
                    
                    <div className="item-details">
                      <div className="item-price">
                        <strong>{formatPrice(item.pricePerDay)}/день</strong>
                      </div>
                      <div className="item-location">
                        📍 {item.location}
                      </div>
                      <div className="item-category">
                        {categories.find(cat => cat.value === item.category)?.label || item.category}
                      </div>
                    </div>

                    <div className="item-actions">
                      <button 
                        className="view-item-btn"
                        onClick={() => navigate(`/items/${item.id}`)}
                      >
                        Подробнее
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {/* Пагинация */}
            {totalPages > 1 && (
              <div className="pagination">
                <button 
                  onClick={() => handlePageChange(currentPage - 1)}
                  disabled={currentPage === 0}
                  className="pagination-btn"
                >
                  ← Предыдущая
                </button>
                
                <div className="page-info">
                  Страница {currentPage + 1} из {totalPages}
                </div>
                
                <button 
                  onClick={() => handlePageChange(currentPage + 1)}
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
    </div>
  );
};

export default ItemSearch;
