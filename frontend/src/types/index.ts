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
  roles: string[];
}

export interface AuthResponse {
  accessToken: string;
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
}

export interface UserResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  description?: string;
  rating?: number;
  verified: boolean;
  enabled: boolean;
  roles: string[];
}

// Типы для аренды
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

// Контекст аутентификации
export interface AuthContextType {
  user: AuthResponse | null;
  login: (credentials: LoginRequest) => Promise<void>;
  register: (userData: UserCreateRequest) => Promise<void>;
  logout: () => void;
  isLoading: boolean;
  error: string | null;
}
