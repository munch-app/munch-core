import {Node} from 'tiptap'
import ContentEditorPlace from './ContentEditorPlace.vue'

export default class Place extends Node {
  get name() {
    return 'place'
  }

  get schema() {
    return {
      attrs: {
        placeId: {
          default: null,
        },
        placeName: {
          default: null,
        },
      },
      group: 'block',
      draggable: true,

      parseDOM: [{tag: 'unknown',}],
      toDOM: node => ['unknown', node.attrs],
    }
  }

  commands({type}) {
    return (attrs) => (state, dispatch) => {
      const node = type.create(attrs)
      dispatch(state.tr.replaceSelectionWith(node))
    }
  }

  get view() {
    return ContentEditorPlace
  }
}
