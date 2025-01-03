import React, { useState, useEffect } from 'react';
import PartidaView from './PartidaView';

function RenderContent({ contenido }) {
  const [res, setRes] = useState(contenido);

  useEffect(() => {
    if (contenido.search(/{[A-Z0-9]{5}}/) !== -1) {
      let codigo = contenido.match(/{[A-Z0-9]{5}}/)[0].slice(1, -1);
      fetch(
        "/api/v1/partida/search?codigo=" + codigo,
        {
          method: "GET"
        }
      )
        .then((response) => response.json())
        .then((data) => {
          setRes(<PartidaView game={data} interfaz='chat' />);
        })
        .catch((message) =>{
            setRes('Error al cargar la invitación a la partida ' + codigo);

        });
    } else {
      setRes(contenido);
    }
  }, [contenido]);

  return (
    <div>{res}</div>
  );
}

export default RenderContent;