import html2Canvas from 'html2canvas'
import JsPDF from 'jspdf'

export default {
  install(Vue, options) {
    Vue.prototype.getPdf = function (title, selector) {
      html2Canvas(document.querySelector(selector), {
        allowTaint: true,
        dpi: window.devicePixelRatio
      }).then(function (canvas) {
          let contentWidth = canvas.width;
          let contentHeight = canvas.height;
          console.log(contentHeight / contentWidth);
          // 边距
          let padding = {left: 40, top: 50};
          let pdfImageWidth = 595.28 - padding.left * 2;
          let pdfImageHeight = pdfImageWidth / contentWidth * contentHeight;
          let pageData = canvas.toDataURL('image/jpeg', 1.0);
          console.log("pageData=" + pageData);
          let PDF = new JsPDF('', 'pt', 'a4');
          console.log("pdfImageWidth=" + pdfImageWidth + "pdfImageHeight=" + pdfImageHeight);
          PDF.addImage(pageData, 'JPEG', padding.left, padding.top, pdfImageWidth, pdfImageHeight);

          PDF.save(title + '.pdf')
        }
      )
    }
  }
}
