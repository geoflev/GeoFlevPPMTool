import { GET_PROJECTS, GET_PROJECT, DELETE_PROJECT } from "../actions/types";

const initialState = {
  projects: [],
  project: {},
};

export default function (state = initialState, action) {
  switch (action.type) {
    case GET_PROJECTS:
      return { ...state, projects: action.payload };

    case GET_PROJECT:
      return {
        ...state,
        project: action.payload,
      };

    //2nd STEP to add an operation
    //next is projectActions.js
    case DELETE_PROJECT:
      return {
        ...state,
        projects: state.projects.filter(
          //here we say, ok delete it from the server but also in the state
          //so we dont have to refresh the page so we can see the change
          (project) => project.projectIdentifier !== action.payload
        ),
      };
    default:
      return state;
  }
}
