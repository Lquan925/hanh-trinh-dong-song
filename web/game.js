(() => {
  'use strict';
  const $ = id => document.getElementById(id);
  const canvas = $('game'), ctx = canvas.getContext('2d');
  const W = 1280, H = 720, ROUND_SECONDS = 240;
  const background = new Image();
  const species = [
    {water:false,frames:Array.from({length:6},(_,i)=>'bird'+(i+1)+'.png')},
    {water:false,frames:Array.from({length:8},(_,i)=>'bluebird'+(i+1)+'.png')},
    {water:true,frames:['fish1.png']}, {water:true,frames:['fish2.png']},
    {water:true,frames:['shrimp.png']}, {water:true,frames:['turtle.png']}
  ];
  const pictures = {};
  let creatures = [], animalCount = 0, stateTime = 0, cooldown = 3;
  function spawnCreature(type = Math.floor(Math.random()*species.length)) {
    if (creatures.length >= 30) return;
    const direction = Math.random() < .5 ? 1 : -1;
    creatures.push({type,x:direction>0?-100:W,baseY:species[type].water?rand(440,580):rand(100,270),y:0,w:100,h:100,speed:45*direction});
  }
  function addCreature() { if(mode==='playing' && cooldown >= 3) {spawnCreature();cooldown=0;} }
  $('addAnimal').addEventListener('click',addCreature);
  const music = new Audio('assets/nhacnen.mp3?v=b97a75930007');
  music.loop = true; music.volume = .35;
  let mode = 'loading', elapsed = 0, score = 0, spawnTime = 2.4, last = 0, musicEnabled = true, character = 'Nam';
  let bubbles = [], flashes = [];
  let touchHeld = false, pointer = null;
  const rand = (a,b) => a + Math.random() * (b-a);
  function playMusic() { if (musicEnabled && mode === 'playing') music.play().catch(() => {}); }
  function hud() {
    $('score').textContent = String(score + animalCount*10).padStart(3,'0');
    $('count').textContent = animalCount + ' con / ' + score + ' b\u00f3ng';
    const time = Math.max(0, Math.ceil(ROUND_SECONDS-elapsed));
    $('time').textContent = String(Math.floor(time/60)).padStart(2,'0') + ':' + String(time%60).padStart(2,'0');
  }
  function start() {
    if (mode === 'paused') { resume(); return; }
    score = 0; animalCount = 0; creatures = []; stateTime = 0; cooldown = 3; elapsed = 0; spawnTime = 0; bubbles = []; flashes = []; pointer = null; touchHeld = false;
    mode = 'playing'; for(let i=0;i<species.length;i++)spawnCreature(i); $('overlay').classList.add('hidden'); $('pause').disabled = false; hud(); playMusic();
  }
  function pause() {
    if (mode !== 'playing') return;
    mode = 'paused'; touchHeld = false; music.pause();
    $('overlayTag').textContent = 'T\u1ea0M D\u1eeaNG';
    $('overlayTitle').textContent = 'Ngh\u1ec9 m\u1ed9t ch\u00fat nh\u00e9!';
    $('overlayText').textContent = 'Nh\u1ea5n ti\u1ebfp t\u1ee5c khi b\u1ea1n s\u1eb5n s\u00e0ng.';
    $('characters').hidden = true; $('start').textContent = 'Ti\u1ebfp t\u1ee5c'; $('overlay').classList.remove('hidden');
  }
  function resume() { mode = 'playing'; $('overlay').classList.add('hidden'); playMusic(); }
  function finish() {
    mode = 'finished'; touchHeld = false; music.pause(); $('pause').disabled = true;
    $('overlayTag').textContent = 'HO\u00c0N TH\u00c0NH'; $('overlayTitle').textContent = 'B\u1ea1n b\u1eaft \u0111\u01b0\u1ee3c ' + (score + animalCount*10) + ' \u0111i\u1ec3m!';
    $('overlayText').textContent = 'M\u1ed7i con v\u1eadt 10 \u0111i\u1ec3m, m\u1ed7i bong b\u00f3ng 1 \u0111i\u1ec3m. C\u00f9ng ch\u01a1i l\u1ea1i nh\u00e9!';
    $('characters').hidden = false; $('start').textContent = 'Ch\u01a1i l\u1ea1i'; $('overlay').classList.remove('hidden');
    try { const history = JSON.parse(localStorage.getItem('river-bubble-history') || '[]'); history.unshift({date:new Date().toISOString(),character,bubbles:score,animals:animalCount,score:score+animalCount*10,version:'2.0-aquatic'}); localStorage.setItem('river-bubble-history',JSON.stringify(history.slice(0,50))); } catch {}
  }
  function catchBubbles() {
    if (!pointer || mode !== 'playing') return;
    for (let i = bubbles.length-1; i >= 0; i--) {
      const b = bubbles[i];
      if (pointer.x >= b.x-5 && pointer.x <= b.x+45 && pointer.y >= b.y-5 && pointer.y <= b.y+45) {
        flashes.push({x:b.x,y:b.y,life:.6}); bubbles.splice(i,1); score++;
      }
    }
    hud();
  }
  function catchCreature() {
    for(let i=creatures.length-1;i>=0;i--){const a=creatures[i];if(pointer.x>=a.x+20 && pointer.x<=a.x+80 && pointer.y>=a.y+20 && pointer.y<=a.y+80){creatures.splice(i,1);animalCount++;flashes.push({x:a.x,y:a.y,life:.6,points:10});break;}}
    hud();
  }
  function update(dt) {
    elapsed = Math.min(ROUND_SECONDS,elapsed+dt);
    if (elapsed >= ROUND_SECONDS) { hud(); finish(); return; }
    stateTime += dt; cooldown += dt;
    $('addAnimal').disabled = cooldown < 3 || creatures.length >= 30;
    creatures.forEach(a=>{a.x+=a.speed*dt;a.y=a.baseY+Math.sin(stateTime*4)*(species[a.type].water?15:60);});
    creatures=creatures.filter(a=>a.x>=-a.w && a.x<=W);
    if(!creatures.length)spawnCreature();
    bubbles.forEach(b => b.y += 40*dt); bubbles = bubbles.filter(b => b.y <= H);
    if (touchHeld) catchBubbles();
    spawnTime += dt;
    if (spawnTime >= 2.4) { creatures.forEach(a=>{if(a.x>=0 && a.x+a.w<=W)bubbles.push({x:a.x+30,y:a.y+30});}); spawnTime %= 2.4; }
    flashes.forEach(f => {f.life -= dt; f.y -= 35*dt;}); flashes = flashes.filter(f => f.life > 0); hud();
  }
  function draw() {
    ctx.clearRect(0,0,W,H); if (background.complete && background.naturalWidth) ctx.drawImage(background,0,0,W,H);
    bubbles.forEach(b => {
      ctx.beginPath(); ctx.arc(b.x+20,b.y+20,16,0,Math.PI*2); ctx.fillStyle='rgba(77,204,255,.5)'; ctx.fill();
      ctx.strokeStyle='rgba(26,230,255,.95)'; ctx.lineWidth=2; ctx.stroke();
      ctx.beginPath(); ctx.arc(b.x+15,b.y+14,4,0,Math.PI*2); ctx.fillStyle='rgba(255,255,255,.85)'; ctx.fill();
    });
    creatures.forEach(a=>{const frames=species[a.type].frames;const img=pictures[frames[Math.floor(stateTime/.15)%frames.length]];if(!img)return;ctx.save();if(a.speed>0){ctx.translate(a.x+a.w,a.y);ctx.scale(-1,1);ctx.drawImage(img,0,0,a.w,a.h);}else ctx.drawImage(img,a.x,a.y,a.w,a.h);ctx.restore();});
    flashes.forEach(f => {ctx.save();ctx.globalAlpha=f.life/.6;ctx.fillStyle='#164f35';ctx.font='bold 26px system-ui';ctx.fillText('+'+(f.points||1),f.x,f.y);ctx.restore();});
  }
  function frame(t) { const dt = last ? Math.min((t-last)/1000,.05) : 0; last=t; if(mode==='playing')update(dt); if(mode!=='loading' && mode!=='error')draw(); requestAnimationFrame(frame); }
  function locate(e) { const r=canvas.getBoundingClientRect(); pointer={x:(e.clientX-r.left)*W/r.width,y:(e.clientY-r.top)*H/r.height}; }
  canvas.addEventListener('pointerdown',e=>{if(mode!=='playing')return;e.preventDefault();canvas.setPointerCapture(e.pointerId);locate(e);touchHeld=true;catchBubbles();catchCreature();});
  canvas.addEventListener('pointermove',e=>{locate(e);if(touchHeld)catchBubbles();});
  ['pointerup','pointercancel','lostpointercapture'].forEach(name=>canvas.addEventListener(name,()=>{touchHeld=false;}));
  $('start').addEventListener('click',start); $('pause').addEventListener('click',pause);
  $('music').addEventListener('click',()=>{musicEnabled=!musicEnabled;$('music').setAttribute('aria-pressed',String(musicEnabled));$('music').textContent=musicEnabled?'T\u1eaft nh\u1ea1c':'B\u1eadt nh\u1ea1c';if(musicEnabled)playMusic();else music.pause();});
  window.addEventListener('keydown',e=>{if(e.key==='Escape'){if(mode==='playing')pause();else if(mode==='paused')resume();}});
  window.addEventListener('blur',pause); document.addEventListener('visibilitychange',()=>{if(document.hidden)pause();});
  document.querySelectorAll('[data-character]').forEach(button=>button.addEventListener('click',()=>{character=button.dataset.character;document.querySelectorAll('[data-character]').forEach(b=>{b.classList.toggle('chosen',b===button);b.setAttribute('aria-pressed',String(b===button));});}));
  function ready() {mode='ready';$('start').disabled=false;$('start').textContent='B\u1eaft \u0111\u1ea7u ch\u01a1i';draw();};
  background.onerror=()=>{mode='error';$('loadError').textContent='Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c h\u00ecnh n\u1ec1n. H\u00e3y t\u1ea3i l\u1ea1i trang.';};
  function loadImage(img,file) {return new Promise((resolve,reject)=>{img.onload=resolve;img.onerror=reject;img.src='assets/'+file;});}
  Promise.all([loadImage(background,'background.jpg'),...species.flatMap(a=>a.frames).map(file=>{const img=new Image();pictures[file]=img;return loadImage(img,file);})]).then(ready).catch(()=>{mode='error';$('loadError').textContent='Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c h\u00ecnh. H\u00e3y t\u1ea3i l\u1ea1i trang.';});
  requestAnimationFrame(frame);
})();
