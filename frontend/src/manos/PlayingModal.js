import { useState, forwardRef, useEffect, useRef } from 'react';
import tokenService from "frontend/src/services/token.service.js";
import useFetchState from "../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

const PlayingModal = forwardRef((props, ref) => {
    const game = props.game
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [mano, setMano] = useState(null)

    const [draggedCarta, setDraggedCarta] = useState(null);
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 });


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
                (cartasJugador &&
                (<img
                    key={index}
                    src={carta.foto} 
                    alt={`Carta ${index + 1}`}
                    style={{ width: '50px', height: '75px', margin: '5px' }} 
                    onError={(e) => (e.target.style.display = 'none')} 
                    draggable 
                    onDragStart={(evento) => dragStart(evento, carta)} 
                    onDrag={(evento) => onDrag(evento)} 
                    onDragEnd={(evento) => onDragEnd(evento)} 
                    
                />)
                )
                
            ));
        }

        return <div>Cargando cartas...</div>;
    };

    const dragStart = (evento, carta) => {
        setDraggedCarta(carta);
        evento.dataTransfer.effectAllowed ='move';
        
    }
    const onDrag = (evento) => { setPositionCarta({ x: evento.clientX, y: evento.clientY })};
    const onDragEnd = (evento) => { setPositionCarta({ x: evento.clientX, y: evento.clientY }) };

    const handleDrop = (evento) => {
        evento.preventDefault();
        // Aquí puedes manejar el caso de soltar la carta
        const offsetX = evento.clientX;
        const offsetY = evento.clientY;
        setPositionCarta({ x: offsetX, y: offsetY });
        
    };
    const allowDrop = (evento) => {
        evento.preventDefault();
    };

    



    return (<>
            <div /*  onDrop={handleDrop} onDragOver={allowDrop} */>
                <h3>Cartas del Jugador {posicion + 1}</h3>
                {renderCartasJugador()} 
                {draggedCarta && 
                  <img
                  src={draggedCarta.foto} 
                  alt='Carta arrastrada'
                  style={{ position: 'absolute', left: `${positionCarta.x}px`, top: `${positionCarta.y}px`, width: '50px', height: '75px' }}
                  onError={(e) => (e.target.style.display = 'none')}
                  />  }
            </div>
        </>
    )
        
    

});
export default PlayingModal;