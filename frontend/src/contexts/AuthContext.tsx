import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { AuthContextType, AuthResponse, LoginRequest, UserCreateRequest } from '../types';
import { authApi } from '../services/api';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth должен использоваться внутри AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<AuthResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Проверяем наличие сохраненного пользователя при загрузке
  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    const token = localStorage.getItem('accessToken');
    
    if (savedUser && token) {
      setUser(JSON.parse(savedUser));
    }
    setIsLoading(false);
  }, []);

  const login = async (credentials: LoginRequest) => {
    try {
      setIsLoading(true);
      setError(null);
      
      const authResponse = await authApi.login(credentials);
      
      // Сохраняем токен и данные пользователя
      localStorage.setItem('accessToken', authResponse.accessToken);
      localStorage.setItem('user', JSON.stringify(authResponse));
      
      setUser(authResponse);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Ошибка входа в систему';
      setError(errorMessage);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const register = async (userData: UserCreateRequest) => {
    try {
      setIsLoading(true);
      setError(null);
      
      const authResponse = await authApi.register(userData);
      
      // Сохраняем токен и данные пользователя
      localStorage.setItem('accessToken', authResponse.accessToken);
      localStorage.setItem('user', JSON.stringify(authResponse));
      
      setUser(authResponse);
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Ошибка регистрации';
      setError(errorMessage);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    setUser(null);
    setError(null);
  };

  const value: AuthContextType = {
    user,
    login,
    register,
    logout,
    isLoading,
    error,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
