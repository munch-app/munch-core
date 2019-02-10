import {Node} from 'tiptap'
import ContentEditorLine from './ContentEditorLine.vue'

export default class Line extends Node {
  get name() {
    return 'line'
  }

  get schema() {
    return {
      group: 'block',
      draggable: true,

      parseDOM: [{tag: 'div',}],
      toDOM: node => ['div', node.attrs],
    }
  }

  commands({type}) {
    return (attrs) => (state, dispatch) => {
      const node = type.create(attrs)
      dispatch(state.tr.replaceSelectionWith(node))
    }
  }

  get view() {
    return ContentEditorLine
  }
}
