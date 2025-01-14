import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import Logout from './index';
import tokenService from '../../services/token.service';

jest.mock('../../services/token.service');

describe('Logout Component', () => {
  beforeEach(() => {
    tokenService.getLocalAccessToken.mockReturnValue('mock-jwt-token');
    tokenService.removeUser = jest.fn();

    jest.spyOn(window.localStorage.__proto__, 'removeItem').mockImplementation(jest.fn());

    delete window.location;
    window.location = {
      href: '',
      pathname: '', 
    };
  });

  test('renders logout confirmation message', () => {
    render(
      <Router>
        <Logout />
      </Router>
    );

    expect(screen.getByText('¿Seguro que querés cerrar sesión?')).toBeInTheDocument();
  });

  test('calls sendLogoutRequest on "Sí" button click', () => {
    render(
      <Router>
        <Logout />
      </Router>
    );

    const yesButton = screen.getByText('Sí');
    fireEvent.click(yesButton);

    expect(tokenService.removeUser).toHaveBeenCalled();
    expect(window.localStorage.removeItem).toHaveBeenCalledWith('jwt');
    expect(window.location.href).toBe('/');
  });

  test('navigates to home on "No" button click', () => {
    render(
      <Router>
        <Logout />
      </Router>
    );

    const noButton = screen.getByText('No');
    fireEvent.click(noButton);

    window.location.pathname = '/home';
    expect(window.location.pathname).toBe('/home');
  });
});
