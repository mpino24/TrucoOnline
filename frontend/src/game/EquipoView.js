import { forwardRef, useEffect, useState } from 'react';
import JugadorView from "frontend/src/components/JugadorView.js";
import { CiCirclePlus } from "react-icons/ci";
import tokenService from "frontend/src/services/token.service.js";

const EquipoView = forwardRef((props, ref) => {

    const usuario = tokenService.getUser();



    function changeTeam(){
        //Comprobamos si en la lista de jugadores del equipo ya está el usuario
        const jugador = props.jugadores.find(j=> j.player.id === usuario.id)
        if(jugador){
            alert("Ya perteneces a este equipo")

        }else{
            fetch(
                "/api/v1/partidajugador/changeteam?userId=" + usuario.id,
                {
                    method: "PATCH"
                }
            )
                .then((response) =>{
                    response.text()
                } )
                .catch((message) => alert(message));

        }
    }
    function getJugadoresList() {
        const jugadoresList = props.jugadores.map(j=> j.player).map((player) => {
            return (
                <div style={{marginBottom:5}}>

                <JugadorView
                    jugador={player}
                    isFriend={true}
                />
                </div>
            )
        });

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
                    <p style={{ margin: 0 }}>Unirse al equipo</p>
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