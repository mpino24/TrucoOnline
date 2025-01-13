import React, { useState, forwardRef} from 'react';
import { Link } from "react-router-dom";
import "../static/css/mano/finishModal.css"
import useFetchState from "../util/useFetchState";
import tokenService from "../services/token.service.js";

const jwt = tokenService.getLocalAccessToken();
const FinishedModal = forwardRef((props, ref) => {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

     const [posicion, setPosicion] = useFetchState(
            {}, `/api/v1/partidajugador/miposicion/${props.game.id}`, jwt, setMessage, setVisible
        );
    const ganador= props.game.puntosEquipo1 === props.game.puntosMaximos?0:1;
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
        
        <div style={{display:'flex', flexDirection:'vertical'}}>
            <h2 className="player-heading" style={{left:'72%'}}> Ganador: Equipo {ganador +1}</h2>
            <h3 className="player-heading" style={{marginTop:'80px',left:'77%'}}>{posicion%2===ganador ? "Ganaste": "Perdiste"}</h3>
            </div>
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