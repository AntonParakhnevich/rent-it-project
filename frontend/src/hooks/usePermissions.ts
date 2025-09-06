import { useCallback, useMemo } from 'react';
import { useAuth } from '../contexts/AuthContext';

export type UserRole = 'ADMIN' | 'LANDLORD' | 'RENTER';

export const usePermissions = () => {
  const { user } = useAuth();

  const hasRole = useCallback((role: UserRole): boolean => {
    return user?.roles?.includes(role) || false;
  }, [user?.roles]);

  const hasAnyRole = useCallback((roles: UserRole[]): boolean => {
    return roles.some(role => hasRole(role));
  }, [hasRole]);

  const canCreateItems = useCallback((): boolean => {
    // Согласно SecurityConfig: POST /items требует роль LANDLORD
    return hasRole('LANDLORD');
  }, [hasRole]);

  const canConfirmRentals = useCallback((): boolean => {
    // Согласно SecurityConfig: POST /rentals/confirm/** требует роль LANDLORD
    return hasRole('LANDLORD');
  }, [hasRole]);

  const canViewRentals = useCallback((): boolean => {
    // Согласно SecurityConfig: GET /rentals/** доступно для LANDLORD, RENTER, ADMIN
    return hasAnyRole(['LANDLORD', 'RENTER', 'ADMIN']);
  }, [hasAnyRole]);

  const canViewUsers = useCallback((): boolean => {
    // Согласно SecurityConfig: GET /users/** доступно для LANDLORD, RENTER, ADMIN
    return hasAnyRole(['LANDLORD', 'RENTER', 'ADMIN']);
  }, [hasAnyRole]);

  const isLandlord = useCallback((): boolean => hasRole('LANDLORD'), [hasRole]);
  const isRenter = useCallback((): boolean => hasRole('RENTER'), [hasRole]);
  const isAdmin = useCallback((): boolean => hasRole('ADMIN'), [hasRole]);

  const userRoles = useMemo(() => user?.roles || [], [user?.roles]);

  return {
    hasRole,
    hasAnyRole,
    canCreateItems,
    canConfirmRentals,
    canViewRentals,
    canViewUsers,
    isLandlord,
    isRenter,
    isAdmin,
    userRoles
  };
};
