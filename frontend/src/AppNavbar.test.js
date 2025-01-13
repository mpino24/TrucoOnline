import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import AppNavbar from './AppNavbar';
import tokenService from './services/token.service';
import jwt_decode from 'jwt-decode';

// Mock the dependencies
jest.mock('./services/token.service');
jest.mock('jwt-decode');
jest.mock('./components/LeavingGameModal', () => () => <div>LeavingGameModal</div>);

describe('AppNavbar', () => {
    let mockNavigate;

    beforeEach(() => {
        // Mock localStorage and tokenService
        tokenService.getLocalAccessToken.mockReturnValue('mocked-jwt-token');
        jwt_decode.mockReturnValue({ authorities: ['ADMIN'] });

        // Mocking useNavigate
        mockNavigate = jest.fn();
        jest.mock('react-router-dom', () => ({
            ...jest.requireActual('react-router-dom'),
            useNavigate: () => mockNavigate,
        }));

        // Mock global fetch response
        global.fetch = jest.fn(() =>
            Promise.resolve({
                status: 200,
                json: () => Promise.resolve({ game: { estado: 'EN_PROGRESO' } }),
            })
        );
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test('renders logo and handles click', async () => {
        render(
            <Router>
                <AppNavbar />
            </Router>
        );

        const logo = screen.getByAltText('logo');
        expect(logo).toBeInTheDocument();

        fireEvent.click(logo);

        // Wait for the fetch call and modal check
        await waitFor(() => {
            expect(global.fetch).toHaveBeenCalledWith('/api/v1/partidajugador/partidaJugadorActual', {
                method: 'GET',
                headers: {
                    Authorization: 'Bearer mocked-jwt-token',
                },
            });
        });

        // The modal should be shown if the game state is not finished
        await waitFor(() => {
            expect(screen.getByText('LeavingGameModal')).toBeInTheDocument();
        });

        // Clean up fetch mock
        global.fetch.mockClear();
    });

    test('does not render admin links when user does not have ADMIN role', () => {
        // Mocking JWT decode to return a user without ADMIN role
        jwt_decode.mockReturnValue({ authorities: ['USER'] });

        render(
            <Router>
                <AppNavbar />
            </Router>
        );

        const adminLink = screen.queryByText('Users');
        expect(adminLink).not.toBeInTheDocument();
    });

    test('opens LeavingGameModal when game is not finished', async () => {
        render(
            <Router>
                <AppNavbar />
            </Router>
        );

        const logo = screen.getByAltText('logo');
        fireEvent.click(logo);

        // Check if the modal appears when the game state is not finished
        const modal = await screen.findByText('LeavingGameModal');
        expect(modal).toBeInTheDocument();

        // Clean up fetch mock
        global.fetch.mockClear();
    });

    
});
