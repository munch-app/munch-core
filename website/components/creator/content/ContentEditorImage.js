import {Node} from 'tiptap'
import ContentEditorImage from './ContentEditorImage.vue'

export default class Image extends Node {
  get name() {
    return 'image'
  }

  get schema() {
    return {
      attrs: {
        image: {
          default: null,
        },
        caption: {
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
    return ContentEditorImage
  }
}
