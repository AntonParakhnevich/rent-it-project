import React, { useState, useMemo } from 'react';
import './AvailabilityCalendar.css';

interface AvailabilityCalendarProps {
  itemId: number;
  unavailableDates: string[];
}

const AvailabilityCalendar: React.FC<AvailabilityCalendarProps> = ({ 
  itemId, 
  unavailableDates 
}) => {
  const [currentDate, setCurrentDate] = useState(new Date());

  const monthNames = [
    'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь',
    'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'
  ];

  const dayNames = ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс'];

  // Преобразуем строки дат в Set для быстрого поиска
  const unavailableDatesSet = useMemo(() => {
    return new Set(unavailableDates.map(date => {
      // Нормализуем формат даты к YYYY-MM-DD
      const normalizedDate = new Date(date);
      return normalizedDate.toISOString().split('T')[0];
    }));
  }, [unavailableDates]);

  // Получаем дни текущего месяца
  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    
    // Первый день месяца
    const firstDay = new Date(year, month, 1);
    // Последний день месяца
    const lastDay = new Date(year, month + 1, 0);
    
    // Получаем день недели для первого дня (0 = воскресенье, нужно сделать понедельник = 0)
    let startDayOfWeek = firstDay.getDay();
    startDayOfWeek = startDayOfWeek === 0 ? 6 : startDayOfWeek - 1; // Преобразуем к понедельнику = 0
    
    const daysInMonth = lastDay.getDate();
    const days: Array<{
      day: number | null;
      date: Date | null;
      isCurrentMonth: boolean;
      isToday: boolean;
      isAvailable: boolean;
      isPast: boolean;
    }> = [];

    // Добавляем пустые ячейки для дней предыдущего месяца
    for (let i = 0; i < startDayOfWeek; i++) {
      days.push({
        day: null,
        date: null,
        isCurrentMonth: false,
        isToday: false,
        isAvailable: false,
        isPast: false
      });
    }

    // Добавляем дни текущего месяца
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    for (let day = 1; day <= daysInMonth; day++) {
      const dayDate = new Date(year, month, day);
      const dateString = dayDate.toISOString().split('T')[0];
      const isPast = dayDate < today;
      const isUnavailable = unavailableDatesSet.has(dateString);
      
      days.push({
        day,
        date: dayDate,
        isCurrentMonth: true,
        isToday: dayDate.toDateString() === today.toDateString(),
        isAvailable: !isPast && !isUnavailable,
        isPast
      });
    }

    return days;
  };

  const goToPreviousMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  };

  const goToNextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
  };

  const goToToday = () => {
    setCurrentDate(new Date());
  };

  const days = getDaysInMonth(currentDate);
  const monthYear = `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`;

  // Разбиваем дни на недели
  const weeks = [];
  for (let i = 0; i < days.length; i += 7) {
    weeks.push(days.slice(i, i + 7));
  }

  return (
    <div className="availability-calendar">
      <div className="calendar-header">
        <button 
          className="nav-btn prev"
          onClick={goToPreviousMonth}
          aria-label="Предыдущий месяц"
        >
          ‹
        </button>
        
        <h3 className="month-year">{monthYear}</h3>
        
        <button 
          className="nav-btn next"
          onClick={goToNextMonth}
          aria-label="Следующий месяц"
        >
          ›
        </button>
      </div>

      <div className="calendar-controls">
        <button 
          className="today-btn"
          onClick={goToToday}
        >
          Сегодня
        </button>
      </div>

      <div className="calendar-grid">
        {/* Заголовки дней недели */}
        <div className="days-header">
          {dayNames.map(dayName => (
            <div key={dayName} className="day-header">
              {dayName}
            </div>
          ))}
        </div>

        {/* Дни месяца */}
        <div className="days-grid">
          {weeks.map((week, weekIndex) => (
            <div key={weekIndex} className="week-row">
              {week.map((dayData, dayIndex) => (
                <div
                  key={`${weekIndex}-${dayIndex}`}
                  className={`day-cell ${
                    !dayData.isCurrentMonth ? 'other-month' :
                    dayData.isToday ? 'today' :
                    dayData.isPast ? 'past' :
                    dayData.isAvailable ? 'available' : 'unavailable'
                  }`}
                  title={
                    dayData.date ? (
                      dayData.isPast ? 'Прошедшая дата' :
                      dayData.isAvailable ? 'Доступен для аренды' : 'Недоступен'
                    ) : ''
                  }
                >
                  {dayData.day}
                </div>
              ))}
            </div>
          ))}
        </div>
      </div>

      {/* Легенда */}
      <div className="calendar-legend">
        <div className="legend-item">
          <div className="legend-color available"></div>
          <span>Доступно</span>
        </div>
        <div className="legend-item">
          <div className="legend-color unavailable"></div>
          <span>Занято</span>
        </div>
        <div className="legend-item">
          <div className="legend-color past"></div>
          <span>Прошедшие даты</span>
        </div>
        <div className="legend-item">
          <div className="legend-color today"></div>
          <span>Сегодня</span>
        </div>
      </div>

      {/* Информация */}
      <div className="calendar-info">
        <p>
          <strong>Всего недоступных дат:</strong> {unavailableDates.length}
        </p>
        <p className="info-note">
          Календарь показывает доступность товара на основе существующих бронирований.
          Для точной информации свяжитесь с владельцем.
        </p>
      </div>
    </div>
  );
};

export default AvailabilityCalendar;
