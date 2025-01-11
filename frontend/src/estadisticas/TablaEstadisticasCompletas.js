import React from 'react';

import useFetchState from '../util/useFetchState.js';
import { calcularTiempo } from './calcularTiempo.js';

const TablaEstadisticasCompleta = ({ jwt, setMessage, setVisible, setMostrarTablaEstadisticas, estadisticas }) => {
    const [estadisticasGlobales, setEstadisticasGlobales] = useFetchState({}, '/api/v1/estadisticas/estadisticasGlobales', jwt, setMessage, setVisible);

    const calcularEstadisticas = (data, key, global) => {
        let total = data[key];
        let partidasJugadas = data.partidasJugadas; 
        
        let promedio = 0;

        if(partidasJugadas > 0){
            promedio= total / partidasJugadas
            if(key=== 'partidasJugadas' && global){
                let jugadores = data.jugadoresTotales || 1
                promedio = total /jugadores
            } 
        }

        if (key === 'tiempoJugado') {
            let totalSinTransformar = total
            total = calcularTiempo(total);
            promedio = calcularTiempo(totalSinTransformar, partidasJugadas) + " por partida";
        } else {
            promedio = promedio.toFixed(2);
        }
        return { total, promedio };
    };


    const campos = [
        { key: 'partidasJugadas', label: 'Partidas Jugadas' },
        { key: 'tiempoJugado', label: 'Duración' },
        { key: 'victorias', label: 'Victorias' },
        { key: 'derrotas', label: 'Derrotas' },
        { key: 'partidasA2', label: 'Partidas de 2 jugadores' },
        { key: 'partidasA4', label: 'Partidas de 4 jugadores' },
        { key: 'partidasA6', label: 'Partidas de 6 jugadores' },
        { key: 'numeroFlores', label: 'Número de Flores' },
        { key: 'numeroEnganos', label: 'Número de Engaños' },
        { key: 'quieros', label: 'Quieros' },
        { key: 'noQuieros', label: 'No Quieros' },
        { key: 'partidasConFlor', label: 'Partidas con Flor' },
        { key: 'atrapado', label: 'Atrápado' },
    ];

    const renderTabla = (data, titulo, global) => (
        <div style={{ marginBottom: '30px', width: '100%' }}>
            <h2 style={{ textAlign: 'center', color: 'white' }}>{titulo}</h2>
            <table style={{
                borderCollapse: 'collapse',
                width: '95%',
                height:'70%',
                margin: '0 auto',
                color: 'white',
                textAlign: 'left',
                marginBottom: '10px',
                backgroundColor: 'rgba(48, 158, 148, 0.2)', 
                borderRadius: '5px',
                overflow: 'hidden',
            }}>
                <thead>
                    <tr style={{ backgroundColor: 'rgba(48, 158, 148, 0.5)' }}> 
                        <th style={{ border: '1px solid white', padding: '10px' }}>Dato</th>
                        <th style={{ border: '1px solid white', padding: '10px' }}>Total</th>
                        <th style={{ border: '1px solid white', padding: '10px' }}>Promedio</th>
                    </tr>
                </thead>
                <tbody>
                    {campos.map(({ key, label }) => {
                        const { total, promedio } = calcularEstadisticas(data, key, global);
                        return (
                            <tr key={key}>
                                <td style={{ border: '1px solid white', padding: '10px', backgroundColor: 'rgba(48, 158, 148, 0.1)' }}>{label}</td>
                                <td style={{ border: '1px solid white', padding: '10px', backgroundColor: 'rgba(255, 255, 255, 0.1)' }}>{total}</td>
                                <td style={{ border: '1px solid white', padding: '10px', backgroundColor: 'rgba(255, 255, 255, 0.1)' }}>{promedio}</td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        </div>
    );

    return (
        <div style={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            alignItems: 'center',
            backgroundColor: 'rgba(0, 0, 0, 0.5)', 
            color: 'white',
            height: '100%',
            width:'93%',
            top:'40px'
        }}>
            <div style={{display:'flex', flexDirection:'row'}}>
            {renderTabla(estadisticasGlobales, `Estadísticas Globales de ${estadisticasGlobales.jugadoresTotales} jugadores`, true)}
            {renderTabla(estadisticas, 'Tus estadísticas')}
            </div>
            <button
                onClick={() => setMostrarTablaEstadisticas(false)}
                style={{
                    padding: '10px 20px',
                    backgroundColor: '#309e94',
                    color: 'white',
                    border: 'none',
                    borderRadius: '20px',
                    cursor: 'pointer',
                    transition: 'background-color 0.3s, transform 0.3s',
                    fontSize: '16px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                    width: '10%',
                    textAlign: 'center',
                }}
                onMouseEnter={(e) => {
                    e.target.style.backgroundColor = '#357a74';
                    e.target.style.transform = 'scale(1.05)';
                }}
                onMouseLeave={(e) => {
                    e.target.style.backgroundColor = '#309e94';
                    e.target.style.transform = 'scale(1)';
                }}
            >
                Volver
            </button>
        </div>
    );
};

export default TablaEstadisticasCompleta;
