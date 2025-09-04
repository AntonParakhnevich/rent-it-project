import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Layout from './components/Layout/Layout';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import Dashboard from './components/Dashboard/Dashboard';
import UserProfile from './components/User/UserProfile';
import RentalDetails from './components/Rental/RentalDetails';
import MyRentals from './components/Rental/MyRentals';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <div className="App">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<Layout />}>
            <Route index element={<Navigate to="/dashboard" replace />} />
            <Route path="dashboard" element={<Dashboard />} />
            <Route path="my-rentals" element={<MyRentals />} />
            <Route path="profile/:userId" element={<UserProfile />} />
            <Route path="rental/:rentalId" element={<RentalDetails />} />
          </Route>
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App;
