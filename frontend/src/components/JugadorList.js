import { forwardRef } from 'react';

import JugadorView from './JugadorView';


const JugadorList = forwardRef((props, ref)  => {
    let jugadoresList = [];
    function playerKey(player) {
        let res=player.id;
        if(player.isConnected){
            res+=props.jugadores.length+1;
        }
        if(player.ultimoMensaje){
            res+= player.ultimoMensaje.fechaEnvio.substring(20,26);
        }
        return res;
    }
    if(props.jugadores.length > 0) {
         jugadoresList = props.jugadores.map((player) => {
            return (
                <div key={playerKey(player)} onClick={()=> props.mostrarChat(player)}>
                <JugadorView
                jugador={player}
                
                />
                </div>
            )
        });
    }

    return (

            <div style={{height: '100vh'}}>
                {jugadoresList}
                <div style={{height: '6vh'}}></div>
            </div>

        
    )
}
)
export default JugadorList;