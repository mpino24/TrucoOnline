import React, { useState, useEffect } from 'react';
import PartidaView from './PartidaView'; // Asegúrate de importar PartidaView

function RenderContent({ contenido }) {
  const [res, setRes] = useState(contenido);

  useEffect(() => {
    if (contenido.search(/{[A-Z0-9]{5}}/) !== -1) {
      let codigo = contenido.match(/{[A-Z0-9]{5}}/)[0].slice(1, -1);
      fetch("/api/v1/partida/search?codigo=" + codigo, {
        method: "GET"
      })
        .then((response) => response.json())
        .then((data) => {
          setRes(<PartidaView game={data} interfaz='chat' />);
        })
        .catch((message) => {
          setRes(<p>Error al cargar la invitación a la partida {codigo}</p>);
        });
    } else {
      setRes(<p>{contenido}</p>);
    }
  }, [contenido]);

  return (
    <div>{res}</div>
  );
}

export default RenderContent;