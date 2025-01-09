import React, { useState } from 'react';

// Función para oscurecer el color hexadecimal
const darkenColor = (color, percent) => {
    let r = parseInt(color.substring(1, 3), 16);
    let g = parseInt(color.substring(3, 5), 16);
    let b = parseInt(color.substring(5, 7), 16);

    r = Math.max(0, r - (r * percent));
    g = Math.max(0, g - (g * percent));
    b = Math.max(0, b - (b * percent));

    // Convertimos de nuevo los valores RGB a hexadecimal
    return `#${Math.round(r).toString(16).padStart(2, '0')}${Math.round(g).toString(16).padStart(2, '0')}${Math.round(b).toString(16).padStart(2, '0')}`;
};

const BotonDeCambio = ({ color, textoMostrar, textoOcultar, children }) => {
    const [mostrar, setMostrar] = useState(false); 
    const [hoverColor, setHoverColor] = useState(darkenColor(color, 0.2)); 

    const cambiarMostrar = () => {
        setMostrar(!mostrar);
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: '100%' }}>
            <button
                onClick={cambiarMostrar}
                style={{
                    padding: '10px 20px',
                    backgroundColor: color,
                    color: 'white',
                    border: 'none',
                    borderRadius: '25px',
                    cursor: 'pointer',
                    transition: 'background-color 0.3s, transform 0.3s',
                    fontSize: '16px',
                    marginTop: '10px',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
                    width: '50%', 
                    textAlign: 'center', 
                }}
                onMouseEnter={(e) => {
                    e.target.style.backgroundColor = hoverColor;
                    e.target.style.transform = 'scale(1.05)';
                }}
                onMouseLeave={(e) => {
                    e.target.style.backgroundColor = color;
                    e.target.style.transform = 'scale(1)';
                }}
            >
                {mostrar ? textoOcultar : textoMostrar}
            </button>
            <div style={{ marginTop: '8px', width: '65%%', overflow: 'hidden', transition: 'max-height 0.3s ease', maxHeight: mostrar ? '1000px' : '0' }}>
                {mostrar && (
                    <div style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                        {children}
                    </div>
                )}
            </div>
        </div>
    );
};

export default BotonDeCambio;