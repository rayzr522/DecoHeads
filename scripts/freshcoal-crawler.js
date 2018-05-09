const arr = [];
document.querySelectorAll('.heads').forEach(item => arr.push(item))

const out = arr.map(node => ({
    category: node.classList[1],
    cmd: node.nextSibling.innerText
}));

const pre = document.createElement('pre');
pre.innerText = JSON.stringify(out, null, 4);

document.body.appendChild(pre);
