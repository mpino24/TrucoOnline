
import { useState, forwardRef, useEffect } from 'react';
import JugadorView from "frontend/src/components/JugadorView.js";
import { CiCirclePlus } from "react-icons/ci";


const EquipoView = forwardRef((props, ref) => {
    const [jugadores, setJugadores] = useState([]);

    useEffect(() => {
        fetch(
            `/api/v1/jugador/amigos?userId=4`,
            {
                method: "GET"
            }
        )
            .then((response) => response.text())
            .then((data) => {
                setJugadores(JSON.parse(data))

            })


    })

    function getJugadoresList() {
        const jugadoresList = jugadores.map((player) => {
            return (
                <JugadorView
                    jugador={player}
                    isFriend={true}
                />
            )
        });

        const jugRestantes = Math.max(0, props.partida.numJugadores / 2 - jugadores.length);
        const botones = [];
        for (let i = 0; i < jugRestantes; i++) {
            botones.push(
                <div style={{
                    border: '2px solid black',
                    padding: '20px',
                    display: 'flex',           // Cambiado a flex para alinear horizontalmente
                    alignItems: 'center',      // Centra verticalmente los elementos dentro del contenedor
                    textAlign: 'center',
                    width: 'auto',
                    height: 'auto',
                    cursor: 'pointer'
                }}>
                    <CiCirclePlus style={{ width: 30, height: 30, color: 'black', marginRight: 10 }} />
                    <p style={{ margin: 0 }}>Unirse al equipo {props.equipo}</p>
                </div>


            );


        }
        if (jugadores.length < props.partida.numJugadores / 2) {
            return (
                <div>
                    <p>{jugadoresList}</p>
                    <p>{botones}</p>
                </div>
            )
        }

    }



    return (
        <>
            {getJugadoresList()}
        </>
    )







});
export default EquipoView;