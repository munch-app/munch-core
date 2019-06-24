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
        options: {
          default: null,
          // image, style
        }
      },
      group: 'block',
      inline: false,

      draggable: true,
      content: 'block*',
      selectable: true,

      parseDOM: [{tag: 'unknown-m-place',}],
      toDOM: node => ['unknown-m-place', node.attrs],
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
