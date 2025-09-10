import axios from 'axios';
import { LoginRequest, UserCreateRequest, AuthResponse, UserResponse, RentalResponse, ItemRequest, ItemResponse, PageResponse, ItemSearchFilters, UnavailableDatesResponse } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8078';

// Создаем экземпляр axios с базовой конфигурацией
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Интерцептор для добавления токена авторизации
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Интерцептор для обработки ошибок авторизации
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Удаляем токен и перенаправляем на страницу входа
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// API методы для аутентификации
export const authApi = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  },

  register: async (userData: UserCreateRequest): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/register', userData);
    return response.data;
  },
};

// API методы для пользователей
export const userApi = {
  getById: async (id: number): Promise<UserResponse> => {
    const response = await apiClient.get<UserResponse>(`/users/${id}`);
    return response.data;
  },
};

// API методы для аренды
export const rentalApi = {
  getById: async (id: number): Promise<RentalResponse> => {
    const response = await apiClient.get<RentalResponse>(`/rentals/${id}`);
    return response.data;
  },

  getAllByUserId: async (userId: number): Promise<RentalResponse[]> => {
    const response = await apiClient.get<RentalResponse[]>(`/rentals?userId=${userId}`);
    return response.data;
  },

  // Получить аренды где пользователь - арендатор
  getRentalsByRenter: async (renterId: number, page = 0, size = 20): Promise<PageResponse<RentalResponse>> => {
    const response = await apiClient.get<PageResponse<RentalResponse>>(`/rentals/renter/${renterId}?page=${page}&size=${size}`);
    return response.data;
  },

  // Получить аренды где пользователь - владелец предметов (арендодатель)
  getRentalsByOwner: async (ownerId: number, page = 0, size = 20): Promise<PageResponse<RentalResponse>> => {
    const response = await apiClient.get<PageResponse<RentalResponse>>(`/rentals/owner/${ownerId}?page=${page}&size=${size}`);
    return response.data;
  },

  confirm: async (id: number): Promise<void> => {
    await apiClient.post(`/rentals/confirm?id=${id}`);
  },
};

// API методы для предметов
export const itemApi = {
  create: async (itemData: ItemRequest): Promise<ItemResponse> => {
    const response = await apiClient.post<ItemResponse>('/items', itemData);
    return response.data;
  },

  getById: async (id: number): Promise<ItemResponse> => {
    const response = await apiClient.get<ItemResponse>(`/items/${id}`);
    return response.data;
  },

  getByOwnerId: async (ownerId: number, page: number = 0, size: number = 10): Promise<PageResponse<ItemResponse>> => {
    const response = await apiClient.get<PageResponse<ItemResponse>>(`/items?ownerId=${ownerId}&page=${page}&size=${size}`);
    return response.data;
  },

  // Поиск товаров с фильтрами
  search: async (filters: ItemSearchFilters = {}): Promise<PageResponse<ItemResponse>> => {
    const params = new URLSearchParams();
    
    // Всегда ищем только доступные товары
    params.append('available', 'true');
    
    if (filters.category) params.append('category', filters.category);
    if (filters.maxPrice) params.append('maxPrice', filters.maxPrice.toString());
    if (filters.location) params.append('location', filters.location);
    params.append('page', (filters.page || 0).toString());
    params.append('size', (filters.size || 20).toString());

    const queryString = params.toString();
    // Используем универсальный endpoint для всех поисковых запросов
    const endpoint = '/items';
    
    const response = await apiClient.get<PageResponse<ItemResponse>>(`${endpoint}?${queryString}`);
    return response.data;
  },

  // Получить все доступные товары (для RENTER)
  getAvailable: async (page: number = 0, size: number = 20): Promise<PageResponse<ItemResponse>> => {
    const response = await apiClient.get<PageResponse<ItemResponse>>(`/items?available=true&page=${page}&size=${size}`);
    return response.data;
  },

  // Получить недоступные даты для товара
  getUnavailableDates: async (itemId: number, startDate: string, endDate: string): Promise<UnavailableDatesResponse> => {
    const response = await apiClient.get<UnavailableDatesResponse>(
      `/items/unavailable-dates?itemId=${itemId}&startDate=${startDate}&endDate=${endDate}`
    );
    return response.data;
  },
};

export default apiClient;
