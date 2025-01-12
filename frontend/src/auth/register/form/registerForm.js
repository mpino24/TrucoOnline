import { formValidators } from "../../../validators/formValidators";

export const registerFormInputs = [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
    labelStyle: {
      display: "inline-block",
      color: "rgb(255, 211, 0)",
      textShadow: `
        -1px -1px 0 rgb(169, 59, 0),
         1px -1px 0 rgb(169, 59, 0),
        -1px  1px 0 rgb(169, 59, 0),
         1px  1px 0 rgb(255, 121, 49)
      `
    },
  },
  {
    tag: "Password",
    name: "password",
    type: "password",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
    labelStyle: {
      display: "inline-block",
      color: "rgb(255, 211, 0)",
      textShadow: `
        -1px -1px 0 rgb(169, 59, 0),
         1px -1px 0 rgb(169, 59, 0),
        -1px  1px 0 rgb(169, 59, 0),
         1px  1px 0 rgb(255, 121, 49)
      `
    },
  },
  {
    tag: "First Name",
    name: "firstName",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
    labelStyle: {
      display: "inline-block",
      color: "rgb(255, 211, 0)",
      textShadow: `
        -1px -1px 0 rgb(169, 59, 0),
         1px -1px 0 rgb(169, 59, 0),
        -1px  1px 0 rgb(169, 59, 0),
         1px  1px 0 rgb(255, 121, 49)
      `
    },
  },
  {
    tag: "Last Name",
    name: "lastName",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
    labelStyle: {
      display: "inline-block",
      color: "rgb(255, 211, 0)",
      textShadow: `
        -1px -1px 0 rgb(169, 59, 0),
         1px -1px 0 rgb(169, 59, 0),
        -1px  1px 0 rgb(169, 59, 0),
         1px  1px 0 rgb(255, 121, 49)
      `
    },
  },
  {
    tag: "Email",
    name: "email",
    type: "text",
    defaultValue: null,
    isRequired: false,
    validators:[],
    labelStyle: {
      display: "inline-block",
      color: "rgb(255, 211, 0)",
      textShadow: `
        -1px -1px 0 rgb(169, 59, 0),
         1px -1px 0 rgb(169, 59, 0),
        -1px  1px 0 rgb(169, 59, 0),
         1px  1px 0 rgb(255, 121, 49)
      `
    },
  },
  {
    tag: "Foto",
    name: "photo",
    type: "text",
    defaultValue: null,
    isRequired: false,
    validators: [],
    labelStyle: {
      display: "inline-block",
      color: "rgb(255, 211, 0)",
      textShadow: `
        -1px -1px 0 rgb(169, 59, 0),
         1px -1px 0 rgb(169, 59, 0),
        -1px  1px 0 rgb(169, 59, 0),
         1px  1px 0 rgb(255, 121, 49)
      `
    },
  },
];
