import React, { ReactNode } from 'react';
import { usePermissions, UserRole } from '../../hooks/usePermissions';

interface ProtectedComponentProps {
  children: ReactNode;
  requiredRole?: UserRole;
  requiredRoles?: UserRole[];
  requireAll?: boolean; // true = требуются ВСЕ роли, false = любая из ролей (по умолчанию)
  fallback?: ReactNode;
}

export const ProtectedComponent: React.FC<ProtectedComponentProps> = ({
  children,
  requiredRole,
  requiredRoles,
  requireAll = false,
  fallback = null
}) => {
  const { hasRole, hasAnyRole, userRoles } = usePermissions();

  const hasAccess = (): boolean => {
    // Если указана одна роль
    if (requiredRole) {
      return hasRole(requiredRole);
    }

    // Если указаны несколько ролей
    if (requiredRoles && requiredRoles.length > 0) {
      if (requireAll) {
        // Требуются ВСЕ роли
        return requiredRoles.every(role => hasRole(role));
      } else {
        // Требуется ЛЮБАЯ из ролей
        return hasAnyRole(requiredRoles);
      }
    }

    // Если роли не указаны, показываем для всех аутентифицированных пользователей
    return userRoles.length > 0;
  };

  if (!hasAccess()) {
    return <>{fallback}</>;
  }

  return <>{children}</>;
};

// Компонент специально для арендодателей
export const LandlordOnly: React.FC<{ children: ReactNode; fallback?: ReactNode }> = ({ 
  children, 
  fallback 
}) => (
  <ProtectedComponent requiredRole="LANDLORD" fallback={fallback}>
    {children}
  </ProtectedComponent>
);

// Компонент специально для арендаторов
export const RenterOnly: React.FC<{ children: ReactNode; fallback?: ReactNode }> = ({ 
  children, 
  fallback 
}) => (
  <ProtectedComponent requiredRole="RENTER" fallback={fallback}>
    {children}
  </ProtectedComponent>
);

// Компонент для администраторов
export const AdminOnly: React.FC<{ children: ReactNode; fallback?: ReactNode }> = ({ 
  children, 
  fallback 
}) => (
  <ProtectedComponent requiredRole="ADMIN" fallback={fallback}>
    {children}
  </ProtectedComponent>
);
