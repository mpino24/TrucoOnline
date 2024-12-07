import { useState, useEffect } from "react";
import tokenService from "../services/token.service.js";
import WaitingModal from "../game/WaitingModal.js"
import PlayingModal from "../manos/PlayingModal.js"
import { Link } from "react-router-dom";
import {Button} from "reactstrap";
import FinishedModal from "./FinishedModal";

const jwt = tokenService.getLocalAccessToken();

export default function Game(){
    const currentUrl = window.location.href;
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const codigo = currentUrl.split('partidaCode=')[1].substring(0,5);
    const [game,setGame]= useState(null);
    

    useEffect(() => { //¿Hace falta que sea así?
        let intervalId;

        function fetchGame() {
            
            fetch(
                '/api/v1/partida/search?codigo='+codigo,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                      },
                }
            )
                .then((response) => response.json())
                .then((data) => {
                    setGame(data);
                    if(data.estado === 'FINISHED'){
                        clearInterval(intervalId)
                    }
                })
        }
        fetchGame();

        intervalId = setInterval(fetchGame, 1000);
        
        return () => clearInterval(intervalId)
    },[codigo])



    
    return(
        <div>
            {game && game.estado==='WAITING' && 
            
            <WaitingModal game={game}/>}

            {game && game.estado === 'ACTIVE' &&
            <PlayingModal game={game} />
            }

            
            {game && game.estado === 'FINISHED' &&
                <FinishedModal game={game}/>
            }
            


        </div>


    );
}