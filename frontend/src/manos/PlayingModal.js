import { useState, forwardRef, useEffect } from 'react';
import tokenService from "frontend/src/services/token.service.js";
import useFetchState from "../util/useFetchState";
import CartasVolteadas from './CartasVolteadas';
const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [mano, setMano] = useState(null)
    const [posicion, setPosicion] = useFetchState(
        {}, `/api/v1/partidajugador/miposicion/${game.id}`, jwt, setMessage, setVisible
      );
    
    useEffect(() => {
        let intervalId;

        function fetchGame() {
            
            fetch(
                '/api/v1/mano/search?codigo='+game.codigo,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                      },
                }
            )
                .then((response) => response.json())
                .then((data) => {
                    setMano(data);
                })
        }
        fetchGame();

        intervalId = setInterval(fetchGame, 1000);
        
        return () => clearInterval(intervalId)
    },[game.codigo])

    const renderCartasJugador = () => {
        if (mano && mano.cartasDisp && posicion !== null) {
            const cartasJugador = mano.cartasDisp[posicion]; 

            if (!cartasJugador) {
                return <div>No hay cartas para mostrar.</div>;
            }

            
            return cartasJugador.map((carta, index) => (
                <img
                    key={index}
                    src={carta.foto} 
                    alt={`Carta ${index + 1}`}
                    style={{ width: '50px', height: '75px', margin: '5px' }} 
                    onError={(e) => (e.target.style.display = 'none')} 
                />
            ));
        }

        return <div>Cargando cartas...</div>;
    };

   

    return (<>
            <div>
                <h3>Cartas del Jugador {posicion + 1}</h3>
                <div style={{ position: 'fixed', bottom: '60px', left: '50%', transform: 'translateX(-50%)' }}>
                {renderCartasJugador()}
                </div> 
            </div>
                
            <div>
                <h3>Cartas voltedas otros jugadores</h3>
                {mano && <CartasVolteadas cartasDispo={mano.cartasDisp} posicionListaCartas={posicion} />}
            </div>
        {console.log(mano)}
        {console.log(posicion)}</>
    )
        
    

});
export default PlayingModal;