
import { useState, forwardRef, useEffect } from 'react';
import JugadorView from "frontend/src/components/JugadorView.js";
import { CiCirclePlus } from "react-icons/ci";


const EquipoView = forwardRef((props, ref) => {


    function getJugadoresList() {
        const jugadoresList = props.jugadores.map((player) => {
            return (
                <JugadorView
                    jugador={player}
                    isFriend={true}
                />
            )
        });

        function changeTeam(){
            alert('hola')

        }

        const jugRestantes = Math.max(0, props.partida.numJugadores / 2 - props.jugadores.length);
        const botones = [];
        for (let i = 0; i < jugRestantes; i++) {
            botones.push(
                <div style={{
                    border: '2px solid black',
                    padding: '20px',
                    display: 'flex',      
                    alignItems: 'center',     
                    textAlign: 'center',
                    width: 'auto',
                    height: 'auto',
                    cursor: 'pointer'
                }}
                onClick={() => changeTeam()}
                >
                    <CiCirclePlus style={{ width: 30, height: 30, color: 'black', marginRight: 10 }} />
                    <p style={{ margin: 0 }}>Unirse al equipo {props.equipo}</p>
                </div>


            );


        }
            return (
                <div>
                    <p>{jugadoresList}</p>
                    <p>{botones}</p>
                </div>
            )

    }



    return (
        <>
            {getJugadoresList()}
        </>
    )







});
export default EquipoView;