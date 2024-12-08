import React, { forwardRef} from 'react';
import { Link } from "react-router-dom";
import {Button} from "reactstrap";
import "frontend/src/static/css/mano/finishModal.css"

const FinishedModal = forwardRef((props, ref) => {

    const ganador= props.puntosEquipo1 === props.puntosMaximos?0:1;
    return (
        <div className='finish-modal-container'>
        <div
        style={{ 
            backgroundImage: 'url(/fondos/fondoPlayingModal.jpg)',
            backgroundSize: 'cover', 
            backgroundRepeat: 'no-repeat', 
            backgroundPosition: 'center', 
            height: '19000vh', // Adjusted to cover the viewport
            width: '100vw',
            position: 'relative', // To position the dragged card relative to this container
            zIndex: -1
        }}
    >
            <h2 className="player-heading" style={{left:'40%'}}> Ganador: Equipo {ganador}</h2>
            <div className="finish-button-container">
            <button style={{zIndex: 1000000000}} >
                <Link
                to={`/home`}
                style={{ textDecoration: "none", color: 'white'}}>Volver</Link>
                </button>
            </div>
        </div>
        </div>

    )
})

export default FinishedModal;