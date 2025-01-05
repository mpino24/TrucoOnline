import { forwardRef } from 'react';

import JugadorView from './JugadorView';


const JugadorList = forwardRef((props, ref)  => {
    function playerKey(player) {
        let res=player.id;
        if(player.isConnected){
            res+=props.jugadores.length+1;
        }
        return res;
    }

    const jugadoresList = props.jugadores.map((player) => {
        return (
            <div key={playerKey(player)} onClick={()=> props.mostrarChat(player)}>
            <JugadorView
            jugador={player}
            isFriend={true}
            isSolicitud={false}
            />
            </div>
        )
    });
    return (

            <div style={{height: '100vh'}}>
                {jugadoresList}
                <div style={{height: '6vh'}}></div>
            </div>

        
    )
}
)
export default JugadorList;