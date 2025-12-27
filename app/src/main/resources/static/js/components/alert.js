export function createAlert(container, message, variant) { 
  let div = document.createElement("div");
  let variantValue = {
    sucess:
    {
      background: 'green',
      border: "1px solid green",
      color:"white"
    },
    danger: {
      background: 'red',
      border: "1px solid red",
      color:"white"
    }
  }

  let values = variantValue[variant];
  div.style = `background:${values.background}; border:${values.border}; text-align:center; padding:20px; color:${values.color}`;
  div.innerText = message;
  container.innerHTML = "";
  container.appendChild(div);
}