import React, { useState, useEffect } from 'react';
import jwt_decode from 'jwt-decode';
import LogroComponent from './LogroComponent';  
import useFetchState from '../util/useFetchState.js';

const RenderizarLogros = ({ jwt, setMessage, setVisible }) => {
    const [roles, setRoles] = useState([]);
    const [logrosMios, setLogrosMios] = useState(true);
    const [totalLogros, setTotalLogros] = useFetchState(0, 'https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/logros/total', jwt, setMessage, setVisible);
    const [listaMisLogros, setListaMisLogros] = useFetchState([], 'https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/logros/misLogros', jwt, setMessage, setVisible);
    const [listaLogrosGlobales, setListaLogrosGlobales] = useFetchState([], 'https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/logros', jwt, setMessage, setVisible);

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
        }
    }, [jwt]);

    function cambiarLogros() {
        setLogrosMios(!logrosMios);
    }

    // LOGROS
    const renderLogros = () => {
        let logros;
        const soyAdmin = roles.includes('ADMIN');
        if (logrosMios) {
            logros = listaMisLogros;
        } else {
            logros = listaLogrosGlobales;
        }

        const logrosRestantes = totalLogros - listaMisLogros.length;
        let textoLogrosRestantes = `Todavía te quedan ${logrosRestantes} logros por conseguir`;
        if (logrosRestantes === 1) {
            textoLogrosRestantes = 'Solamente te falta un logro!!!';
        }

        if (logros && logros.length > 0) {
            return (
                <>
                    <h3 style={{ color: 'white', marginBottom: '20px' }}>
                        {logrosMios ? `${listaMisLogros?.length} Logros obtenidos` : soyAdmin ? "Logros globales" : textoLogrosRestantes}
                    </h3>
                    <div style={logrosGridStyle}>
                        {logros.map((logro, index) => (
                            <div key={index} style={logroCardStyle}>
                                <LogroComponent logro={logro} />
                            </div>
                        ))}
                    </div>
                </>
            );
        } else {
            return (
                <>
                    {logrosMios && (
                        <div>
                            <h3 style={{ color: 'white' }}>No tenés ningún logro todavía</h3>
                            <p style={{ color: 'white' }}>Seguí jugando para desbloquear logros.</p>
                        </div>
                    )}
                    {!logrosMios && (
                        <div>
                            <h3 style={{ color: 'white' }}>
                                {listaMisLogros.length === totalLogros ? "¡Tienes todos los logros!" : textoLogrosRestantes}
                            </h3>
                            <p style={{ color: 'white' }}>
                                {listaMisLogros.length === totalLogros ? "Realmente eres el amo del Truco" : "¡Mucha suerte!"}
                            </p>
                        </div>
                    )}
                </>
            );
        }
    };

    const logrosGridStyle = {
        display: 'grid',
        gridTemplateColumns: 'repeat(2, 1fr)',
        gap: '20px',
        maxHeight: '600px',
        overflowY: 'auto',
        padding: '20px',
        backgroundColor: 'rgba(255, 255, 255, 0)',
        borderRadius: '10px',
        scrollbarWidth: 'none',
        msOverflowStyle: 'none',
    };

    const logroCardStyle = {
        border: '1px solid #ddd',
        borderRadius: '10px',
        padding: '10px',
        backgroundColor: 'rgba(0, 0, 0, 0.2)',
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
        textAlign: 'center',
        position: 'relative',
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: '10px' }}>
            {renderLogros()}
            <button
                onClick={cambiarLogros}
                style={{
                    padding: '10px 20px',
                    background: 'linear-gradient(45deg, #ff5722, #ff9800)',
                    color: 'white',
                    border: 'none',
                    borderRadius: '25px',
                    cursor: 'pointer',
                    transition: 'background 0.3s, transform 0.3s',
                    marginTop: '20px',
                    fontSize: '16px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                }}
                onMouseEnter={(e) => {
                    e.target.style.background = 'linear-gradient(45deg, #e64a19, #f57c00)';
                    e.target.style.transform = 'scale(1.05)';
                }}
                onMouseLeave={(e) => {
                    e.target.style.background = 'linear-gradient(45deg, #ff5722, #ff9800)';
                    e.target.style.transform = 'scale(1)';
                }}
            >
                {logrosMios ? (roles.includes('ADMIN') ? "Ver logros globales" : "Ver logros que te faltan") : "Ver tus logros"}
            </button>
        </div>
    );
};

export default RenderizarLogros;
