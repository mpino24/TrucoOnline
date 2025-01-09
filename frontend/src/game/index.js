import { useState, useEffect } from "react";
import tokenService from "../services/token.service.js";
import WaitingModal from "../game/WaitingModal.js";
import PlayingModal from "../manos/PlayingModal.js";
import FinishedModal from "./FinishedModal";
import { useNavigate } from "react-router-dom";

const jwt = tokenService.getLocalAccessToken();

export default function Game() {
    const currentUrl = window.location.href;
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const codigo = currentUrl.split('partidaCode=')[1].substring(0, 5);
    const [game, setGame] = useState(null);
    const navigate = useNavigate();

    
    const handleErrorAndRedirect = (errorMessage) => {
        setMessage(errorMessage);
        setVisible(true);
        setTimeout(() => {
            navigate("/home"); 
        }, 3000);
    };

    useEffect(() => {
        let intervalId;

        async function fetchGame() {
            try {
                const response = await fetch(
                    `/api/v1/partida/search?codigo=${codigo}`,
                    {
                        method: "GET",
                        headers: {
                            Authorization: `Bearer ${jwt}`,
                        },
                    }
                );

                if (!response.ok) {
                    if (response.status === 404) {
                        handleErrorAndRedirect("Partida no encontrada, redirigiendo...");
                    } else {
                        throw new Error("Error en la respuesta del servidor.");
                    }
                } else {
                    const data = await response.json();
                    setGame(data);

                    if (data.estado === "FINISHED") {
                        clearInterval(intervalId); 
                    }
                }
            } catch (error) {
                console.error("Error fetching partida:", error);
                if (!visible) {
                    setMessage("Error al obtener los datos de la partida.");
                    setVisible(true);
                }
            }
        }

        
        fetchGame();
        intervalId = setInterval(fetchGame, 1000);

        return () => clearInterval(intervalId); 
    }, [codigo, navigate]);

    return (
        <div>
            {visible && <div>{message}
            </div>}
            {game && game.estado === "WAITING" && <WaitingModal game={game} />}
            {game && game.estado === "ACTIVE" && <PlayingModal game={game} />}
            {game && game.estado === "FINISHED" && <FinishedModal game={game} />}
        </div>
    );
}
