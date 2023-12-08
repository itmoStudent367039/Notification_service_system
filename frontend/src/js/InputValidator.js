// // const btn1 = document.querySelector("#signUpBtn");
// // const appId = "appNameSignUp";
// // const errorMassage = "Username is not valid";
// // btn1.addEventListener("click", isNotValid);
// export function isNotValid(appId, errorMassage) {
//   const App = ({ initText }) => {
//     const [text] = React.useState(initText);
//     return (
//       <div className="app">
//         <div>
//           <i
//             className="fa fa-exclamation-triangle"
//             aria-hidden="true"
//             style={{ color: "red", fontSize: "13px" }}
//           ></i>
//           <div
//             style={{
//               paddingLeft: "2rem",
//               color: "red",
//               fontSize: "small",
//               paddingTop: "0.3rem",
//             }}
//           >
//             {text}
//           </div>
//         </div>
//       </div>
//     );
//   };
//   const container = document.getElementById(appId);
//   const root = ReactDOM.createRoot(container);
//   root.render(<App initText={errorMassage} />);
// }
