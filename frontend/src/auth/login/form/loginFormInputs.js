import { formValidators } from "../../../validators/formValidators";

export const loginFormInputs = [
  {
    tag: "Username",
    name: "username",
    type: "text",
    defaultValue: "",
    isRequired: true,
    validators: [formValidators.notEmptyValidator],
    // Add this line:
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
    validators: [formValidators.notEmptyValidator],
  },
];
