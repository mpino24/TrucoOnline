import { useState, useEffect, forwardRef } from "react";
import { Form, Label, Input } from "reactstrap";
import tokenService from "../../services/token.service";
const jwt = tokenService.getLocalAccessToken();

function handleModalVisible(setModalVisible, modalVisible) {
  setModalVisible(!modalVisible);
}

const EditLogroModal = forwardRef((props, ref) => {
  const { setEditLogroModal, editLogroModal, logro } = props;
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    valor: 10,
    metrica: "PARTIDAS_A_2", 
    descripcion: "",
    imagencita: "http://localhost:8080/resources/images/trofeos/trofeo1.jpg",
    oculto: false,
  });

  useEffect(() => {
    if (logro) {
      setFormData({
        name: logro.name,
        valor: logro.valor,
        metrica: logro.metrica,
        descripcion: logro.descripcion,
        imagencita: logro.imagencita,
        oculto: logro.oculto,
      });
    }
  }, [logro]);

  const metricaOptions = [
    "PARTIDAS_JUGADAS",
    "TIEMPO_JUGADO",
    "VICTORIAS",
    "DERROTAS",
    "PARTIDAS_A_2",
    "PARTIDAS_A_4",
    "PARTIDAS_A_6",
    "NUMERO_FLORES",
    "NUMERO_ENGANOS",
    "QUIEROS",
    "NO_QUIEROS",
    "PARTIDAS_CON_FLOR",
    "ATRAPADO"
  ];

  const handleChange = (e) => {
    const target = e.target;
    const type = target.type;
    const checked = target.checked;
    const value = target.value;
    const name = target.name;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(formData);

    fetch(`/api/v1/logros`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then((response) => response.json())
      .then((json) => {
        handleModalVisible(setEditLogroModal, editLogroModal);
        props.setActualizarLista(props.actualizarLista + 1);
      })
      .catch((message) => alert(message));
  };

  return (
    <>
      <Form onSubmit={handleSubmit}>
        <div>
          <Label style={{ color: "orange" }}>Nombre: </Label>
          <Input
            type="text"
            required
            name="name"
            id="name"
            value={formData.name}
            onChange={handleChange}
            className="custom-input"
          />
        </div>
        <div>
          <Label style={{ color: "orange" }}>Valor: </Label>
          <Input
            type="number"
            required
            name="valor"
            id="valor"
            value={formData.valor}
            onChange={handleChange}
            className="custom-input"
          />
        </div>
        <div>
          <Label style={{ color: "orange" }}>Metrica: </Label>
          <div className="switch-container">
            <Input
              type="select"
              name="metrica"
              id="metrica"
              value={formData.metrica}
              onChange={handleChange}
              className="custom-switch"
            >
              {metricaOptions.map((metrica) => (
                <option key={metrica} value={metrica}>
                  {metrica.replaceAll("_", " ")}
                </option>
              ))}
            </Input>
          </div>
        </div>
        <div>
          <Label style={{ color: "orange" }}>Descripcion: </Label>
          <Input
            type="text"
            required
            name="descripcion"
            id="descripcion"
            value={formData.descripcion}
            onChange={handleChange}
            className="custom-input"
          />
        </div>
        <div className="custom-form-input">
          <Label for="photo" className="custom-form-input-label" style={{ color: "orange" }}>
            Foto
          </Label>
          {formData.imagencita && (
            <img
              src={formData.imagencita}
              alt="Foto del perfil"
              style={{
                width: "100px",
                height: "100px",
                borderRadius: "50%",
              }}
              onError={(e) => (e.target.style.display = "none")}
            />
          )}
          <Label for="photo" className="custom-form-input-label" style={{ color: "orange" }}>
            Foto
          </Label>
          <Input
            type="text"
            name="photo"
            id="photo"
            value={formData.imagencita}
            onChange={handleChange}
            className="custom-input"
            placeholder="Poné el link de la foto que quieras usar"
          />
        </div>
        <div>
          <Label style={{ color: "orange" }}>Oculto: </Label>
          <Input
            type="checkbox"
            name="oculto"
            id="oculto"
            checked={formData.oculto}
            onChange={handleChange}
            className="custom-input"
          />
        </div>

        <div className="custom-button-row">
          <button type="submit" className="auth-button">
            Guardar
          </button>
          <button
            type="button"
            onClick={() => handleModalVisible(setEditLogroModal, editLogroModal)}
            className="auth-button"
          >
            Cancelar
          </button>
        </div>
      </Form>
    </>
  );
});

export default EditLogroModal;
