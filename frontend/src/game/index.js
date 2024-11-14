import { useState, useEffect } from "react";
import useFetchState from "frontend/src/util/useFetchState.js";
import tokenService from "frontend/src/services/token.service.js";
import WaitingModal from "frontend/src/game/WaitingModal.js"

const jwt = tokenService.getLocalAccessToken();

export default function Game(){
    const currentUrl = window.location.href;
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const codigo = currentUrl.split('partidaCode=')[1].substring(0,5);
    const [game,setGame]= useFetchState(
        [],
        '/api/v1/partida/search?codigo='+codigo,
        jwt,
        setMessage,
        setVisible
    );
    
    return(
        <div>
            {game.estado==='WAITING' && 
            
            <WaitingModal
            game={game}/>}





        </div>


    );
}