import useFetchState from "../util/useFetchState";
import React, { useState, forwardRef } from 'react';
import tokenService from "../services/token.service";


const jwt = tokenService.getLocalAccessToken();


const Perfil = forwardRef((props, ref) => { 
    const game = props.game; 
    const posicionJugador = props.posicion; 
    const [message, setMessage] = useState(null); 
    const [visible, setVisible] = useState(false); 
    const [perfil, setPerfil] = useFetchState( 
        [], 
        `/api/v1/partidajugador/jugadores/codigoPartida/${game.codigo}`, 
        jwt, 
        setMessage, 
        setVisible, ); 
        console.log(perfil);


    const renderJugador = (persona, index) => { 
        return ( 
            <div key={index}> 
                <p>{persona.userName}</p> 
                <img style={{ height: 60, width: 60, borderRadius: 500 }} 
                        src={persona.foto} 
                        alt={`Foto perfil jugador ${index}`} 
                        onError={(e) => (e.target.style.display = 'none')} /> 
            </div> ); 
        }; 
    
        let posicionUsada=false;
    return ( 
        <> 
            {perfil.map((persona, index) => { 
                if (index !== posicionJugador) { 
                    if (index % 2 === posicionJugador % 2) { 
                        return ( <div key={index} style={{ textAlign: 'center' }}> {renderJugador(persona, index)} </div> ); } 
                        else if (!posicionUsada) { 
                            posicionUsada= true; 
                            return ( <div key={index} style={{ float: 'left' }}> {renderJugador(persona, index)} </div> ); } 
                        else { return ( <div key={index} style={{ float: 'right' }}> {renderJugador(persona, index)} </div> ); } 
                } 
                return null; 
            })} 
        </> 
    );
});

export default Perfil;