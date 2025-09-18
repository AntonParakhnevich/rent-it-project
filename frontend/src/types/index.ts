// Типы для пользователей
export interface LoginRequest {
  email: string;
  password: string;
}

export interface UserCreateRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  description?: string;
  unp?: string; // УНП для арендодателей
  roles: string[];
}

export interface AuthResponse {
  accessToken: string;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
}

export interface UserResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  description?: string;
  unp?: string; // УНП для арендодателей
  rating?: number;
  verified: boolean;
  enabled: boolean;
  roles: string[];
}

// Типы для аренды
export interface RentalRequest {
  itemId: number;
  startDate: string;
  endDate: string;
  totalPrice?: number;
  depositAmount?: number;
  status?: string;
}

export interface RentalResponse {
  id: number;
  itemId: number;
  renterId: number;
  startDate: string;
  endDate: string;
  totalPrice: number;
  depositAmount: number;
  status: string;
}

// Типы для предметов
export interface ItemRequest {
  title: string;
  description: string;
  pricePerDay: number;
  depositAmount: number;
  category: string;
  images: string[];
  available: boolean;
  location: string;
  ownerId: number;
}

export interface ItemResponse {
  id: number;
  title: string;
  description: string;
  pricePerDay: number;
  depositAmount: number;
  category: string;
  images: string[];
  available: boolean;
  location: string;
  ownerId: number;
}

// Типы для пагинации
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// Типы для поиска товаров
export interface ItemSearchFilters {
  category?: string;
  maxPrice?: number;
  location?: string;
  page?: number;
  size?: number;
}

// Типы для недоступных дат
export interface UnavailableDatesResponse {
  itemId: number;
  unavailableDates: string[];
}

// Контекст аутентификации
export interface AuthContextType {
  user: AuthResponse | null;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (userData: UserCreateRequest) => Promise<void>;
  logout: () => void;
  isLoading: boolean;
  error: string | null;
}
