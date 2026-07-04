/* global localStorage */
// src/api/authApi.js

const LOGIN_BASE_URL = 'http://localhost:8080/api/login';
const USER_BASE_URL = 'http://localhost:8080/api/users';

// 1. Iniciar sesion real
export const loginUser = async (email, password) => {
  try {
    const response = await fetch(`${LOGIN_BASE_URL}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      const message = errorData?.message || errorData?.descripcion || 'Credenciales incorrectas o usuario invalido';
      throw new Error(message);
    }

    const data = await response.json();

    if (data.token) {
      localStorage.setItem('token', data.token);
      localStorage.setItem('userId', data.userId || '1');
      localStorage.setItem('userRole', data.role || 'PATIENT');
      localStorage.setItem('username', data.name || 'Usuario');
    }

    return data;
  } catch (error) {
    console.error('Error en loginUser:', error);
    throw error;
  }
};

// 2. Registrar un nuevo paciente desde el flujo publico
export const registerPatient = async (patientData) => {
  try {
    const payload = {
      ...patientData,
      userType: 'PATIENT'
    };

    const response = await fetch(`${USER_BASE_URL}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      const message = errorData?.message || errorData?.descripcion || 'Error al registrar el paciente. El correo podria ya estar en uso.';
      throw new Error(message);
    }

    return await response.json();
  } catch (error) {
    console.error('Error en registerPatient:', error);
    throw error;
  }
};

export const registerUser = registerPatient;

// 3. Cambiar contrasena
export const changePassword = async (email, oldPassword, newPassword) => {
  try {
    const token = localStorage.getItem('token');
    const response = await fetch(`${LOGIN_BASE_URL}/change-password`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      body: JSON.stringify({ email, oldPassword, newPassword })
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      const message = errorData?.message || errorData?.descripcion || 'No se pudo actualizar la contrasena';
      throw new Error(message);
    }

    return await response.json();
  } catch (error) {
    console.error('Error en changePassword:', error);
    throw error;
  }
};
