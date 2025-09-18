import React, { useState } from 'react';
import { rentalApi } from '../../services/api';
import { RentalRequest, ItemResponse } from '../../types';
import { useAuth } from '../../contexts/AuthContext';
// import RentalCalendar from './RentalCalendar'; // Компонент удален
import './RentalForm.css';

interface RentalFormProps {
  item: ItemResponse;
  unavailableDates: string[];
  onRentalCreated: () => void;
  onCancel: () => void;
}

const RentalForm: React.FC<RentalFormProps> = ({ 
  item, 
  unavailableDates, 
  onRentalCreated, 
  onCancel 
}) => {
  const { user } = useAuth();
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleDateClick = (date: Date) => {
    if (!startDate || (startDate && endDate)) {
      // Начинаем новый выбор
      setStartDate(date);
      setEndDate(null);
    } else if (date >= startDate) {
      // Выбираем конечную дату
      setEndDate(date);
    } else {
      // Если выбрана дата раньше начальной, делаем её начальной
      setStartDate(date);
      setEndDate(null);
    }
  };

  const calculateTotalPrice = () => {
    if (!startDate || !endDate) return 0;
    
    const days = Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;
    return days * item.pricePerDay;
  };

  const handleSubmit = async () => {
    if (!startDate || !endDate || !user) {
      setError('Пожалуйста, выберите даты аренды');
      return;
    }

    setIsSubmitting(true);
    setError(null);

    try {
      const rentalRequest: RentalRequest = {
        itemId: item.id,
        startDate: startDate.toISOString(),
        endDate: endDate.toISOString(),
        totalPrice: calculateTotalPrice(),
        depositAmount: item.depositAmount,
        status: 'PENDING'
      };

      await rentalApi.create(rentalRequest);
      onRentalCreated();
    } catch (err: any) {
      setError(err.response?.data?.message || 'Ошибка при создании аренды');
    } finally {
      setIsSubmitting(false);
    }
  };

  const totalPrice = calculateTotalPrice();
  const rentalDays = startDate && endDate 
    ? Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1
    : 0;

  return (
    <div className="rental-form">
      <div className="rental-form-content">
        <div className="rental-form-header">
          <h3>Оформление аренды</h3>
          <button className="close-btn" onClick={onCancel}>×</button>
        </div>
        <div className="rental-form-body">
          <div className="item-summary">
          <h4>{item.title}</h4>
          <p className="item-price">{item.pricePerDay} руб./день</p>
          <p className="item-deposit">Залог: {item.depositAmount} руб.</p>
        </div>

        {/* RentalCalendar компонент удален - функционал перенесен на страницу товара */}
        <div className="calendar-placeholder">
          <p>Календарь перенесен на страницу товара</p>
        </div>

        <div className="rental-summary">
          {startDate && (
            <div className="date-selection">
              <p><strong>Дата начала:</strong> {startDate.toLocaleDateString('ru-RU')}</p>
              {endDate && (
                <>
                  <p><strong>Дата окончания:</strong> {endDate.toLocaleDateString('ru-RU')}</p>
                  <p><strong>Количество дней:</strong> {rentalDays}</p>
                  <p><strong>Стоимость аренды:</strong> {totalPrice} руб.</p>
                  <p><strong>Залог:</strong> {item.depositAmount} руб.</p>
                  <p className="total-cost">
                    <strong>Итого к оплате:</strong> {totalPrice + item.depositAmount} руб.
                  </p>
                </>
              )}
            </div>
          )}
        </div>

        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <div className="rental-form-actions">
          <button 
            onClick={onCancel} 
            className="btn btn-secondary"
            disabled={isSubmitting}
          >
            Отмена
          </button>
          <button 
            onClick={handleSubmit} 
            className="btn btn-primary"
            disabled={!startDate || !endDate || isSubmitting}
          >
            {isSubmitting ? 'Создание...' : 'Создать аренду'}
          </button>
        </div>
        </div>
      </div>
    </div>
  );
};

export default RentalForm;
