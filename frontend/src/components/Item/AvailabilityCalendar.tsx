import React, { useState, useMemo } from 'react';
import './AvailabilityCalendar.css';

interface AvailabilityCalendarProps {
  itemId: number;
  unavailableDates: string[];
  canSelectDates?: boolean;
  startDate?: Date | null;
  endDate?: Date | null;
  onDateSelect?: (startDate: Date | null, endDate: Date | null) => void;
}

const AvailabilityCalendar: React.FC<AvailabilityCalendarProps> = ({ 
  itemId, 
  unavailableDates,
  canSelectDates = false,
  startDate = null,
  endDate = null,
  onDateSelect
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
      isSelected: boolean;
      isInRange: boolean;
      isBlockedForEndSelection: boolean;
    }> = [];

    // Добавляем пустые ячейки для дней предыдущего месяца
    for (let i = 0; i < startDayOfWeek; i++) {
      days.push({
        day: null,
        date: null,
        isCurrentMonth: false,
        isToday: false,
        isAvailable: false,
        isPast: false,
        isSelected: false,
        isInRange: false,
        isBlockedForEndSelection: false
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
      
      const isSelected = canSelectDates && ((startDate && dayDate.getTime() === startDate.getTime()) ||
                        (endDate && dayDate.getTime() === endDate.getTime()));
      
      const isInRange = canSelectDates && startDate && endDate && 
                       dayDate >= startDate && dayDate <= endDate;
      
      // Определяем, недоступна ли дата для выбора как конечная (проверим позже)
      let isBlockedForEndSelection = false;
      if (canSelectDates && startDate && !endDate && dayDate > startDate) {
        // Проверяем диапазон от startDate до dayDate
        const current = new Date(startDate);
        current.setDate(current.getDate() + 1);
        while (current < dayDate) {
          const checkDateString = current.toISOString().split('T')[0];
          if (unavailableDatesSet.has(checkDateString)) {
            isBlockedForEndSelection = true;
            break;
          }
          current.setDate(current.getDate() + 1);
        }
      }
      
      days.push({
        day,
        date: dayDate,
        isCurrentMonth: true,
        isToday: dayDate.toDateString() === today.toDateString(),
        isAvailable: !isPast && !isUnavailable,
        isPast,
        isSelected: !!isSelected,
        isInRange: !!isInRange,
        isBlockedForEndSelection
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

  // Проверяем, есть ли занятые даты в диапазоне между двумя датами
  const hasUnavailableDatesInRange = (start: Date, end: Date): boolean => {
    const current = new Date(start);
    current.setDate(current.getDate() + 1); // Начинаем с дня после начальной даты
    
    while (current < end) {
      const dateString = current.toISOString().split('T')[0];
      if (unavailableDatesSet.has(dateString)) {
        return true;
      }
      current.setDate(current.getDate() + 1);
    }
    return false;
  };

  const handleDateClick = (date: Date, dayData: any) => {
    if (!canSelectDates || !onDateSelect) return;
    
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    // Проверяем, что дата не в прошлом и доступна
    if (date < today || unavailableDatesSet.has(date.toISOString().split('T')[0])) {
      return;
    }

    // Если дата заблокирована для выбора как конечная, не даем её выбрать
    if (dayData.isBlockedForEndSelection) {
      return;
    }

    if (!startDate || (startDate && endDate)) {
      // Начинаем новый выбор
      onDateSelect(date, null);
    } else if (date >= startDate) {
      // Проверяем, есть ли занятые даты в диапазоне
      if (hasUnavailableDatesInRange(startDate, date)) {
        // Если есть занятые даты в диапазоне, начинаем новый выбор с этой даты
        onDateSelect(date, null);
      } else {
        // Выбираем конечную дату
        onDateSelect(startDate, date);
      }
    } else {
      // Если выбрана дата раньше начальной, делаем её начальной
      onDateSelect(date, null);
    }
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
                    dayData.isSelected ? 'selected' :
                    dayData.isInRange && !dayData.isSelected ? 'in-range' :
                    dayData.isBlockedForEndSelection ? 'blocked-for-selection' :
                    dayData.isToday ? 'today' :
                    dayData.isPast ? 'past' :
                    dayData.isAvailable ? 'available' : 'unavailable'
                  } ${canSelectDates && dayData.isAvailable && !dayData.isBlockedForEndSelection ? 'clickable' : ''}`}
                  title={
                    dayData.date ? (
                      dayData.isPast ? 'Прошедшая дата' :
                      dayData.isBlockedForEndSelection ? 'Недоступно - есть занятые даты в диапазоне' :
                      dayData.isAvailable ? (canSelectDates ? 'Кликните для выбора' : 'Доступен для аренды') : 'Недоступен'
                    ) : ''
                  }
                  onClick={() => dayData.date && dayData.isCurrentMonth && handleDateClick(dayData.date, dayData)}
                  style={{ cursor: canSelectDates && dayData.isAvailable && !dayData.isBlockedForEndSelection ? 'pointer' : 'default' }}
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
