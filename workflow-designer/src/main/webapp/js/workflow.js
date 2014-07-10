
		// font specification for labels
		var	gLabelFont			= "10px sans-serif";
		
		// how high is the box around labels
		var	gLabelBoxHeight	= 15;
		
		// color of label text
		var	gLabelColor			= "#000000";
		
		// distance from icon to label
		var	gLabelMargin		= 5;
		
		// color of selected items
		var	gSelectColor		= "#ff00ff";
		
		// icon widths
		var	gObjectWidth		= 20;
		
		// icon heights
		var	gObjectHeight		= 20;
		
		// size of connector arrow
		var	gArrowSize			= 10;
		
		// snap-to grid size
		var	gGridSize			= 40;
		
		// height of the toolbar at the top
		var	gToolBarHeight		= 60;
		
		// the canvas where everything happens
		var	gCanvas				= document.getElementById("workflowcanvas");
		var	gCtx					= gCanvas.getContext("2d");
		
		// current object being dragged.
		var	gCurrentDrag		= null;
		
		// list of workflow objects currently selected
		var	gCurrentSelection	= new Array();
		
		// currently selected new work flow object
		var	gCurrentNew			= null;
		
		// currently selected workflow object label
		var	gCurWFLabelSel		= null;
		
		// currently selected connector label
		var	gCurConLabelSel	= null;
		
		// currently selected new connector
		var	gCurNewConnector	= null;
		
		// setup mouse handling events. 
		gCanvas.onmousedown		= onMouseDown;
		gCanvas.onmouseover		= onMouseOver;
		gCanvas.onmouseout		= onMouseOut;
		gCanvas.onmousemove		= onMouseMove;
		gCanvas.onmouseup			= onMouseUp;
		gCanvas.ondblclick		= onMouseDblClick;
		
		
		
		
		// --------------------------------------------------------------------------------
		// mouse handling routines
		// --------------------------------------------------------------------------------
		
		// called when mouse is clicked in the canvas
		function onMouseDown(e)
			{
			document.onselectstart = function(){ return false; }
			
			// are we selecting or deselecting more than one item
			var	multi		= e.shiftKey > 0 || e.ctrlKey > 0;
			
			// compensate for position of the canvas tag
			var	offset	= getOffset(e.target);
			
			// actual x & y position of themouse with respect to the canvas tag
			var	x			= e.clientX - offset.left;
			var	y			= e.clientY - offset.top;
			
			// see if we are selecting work flow objects
			for (var i = 0;i < gWorkflowObjects.length;i++)
				if (gWorkflowObjects[i].isMouseInside(x, y))
					{
						
					// if we are multi-selecting, then this might be a deselect. 
					if (gWorkflowObjects[i].selected && multi)
						{
						for (var j = 0;j < gCurrentSelection.length;j++)
							if (gCurrentSelection[j] == gWorkflowObjects[i])
								{
									
								// deselect the workflow object
								gWorkflowObjects[i].deselect();
								gCurrentSelection.splice(j, 1);
								if (gCurrentDrag != null)
									gCurrentDrag.stopDrag();
								draw();
								return;
								}
						}
					
					// if the object is already selected, then initiate drag
					if (gWorkflowObjects[i].selected)
						{
						gCurrentDrag	= gWorkflowObjects[i];
						gCurrentDrag.startDrag(x, y);
						return;
						}
					
					// if we are not multi selecting, then desect all other objects
					if (multi == false)
						deselectAll();
						
					// select the object
					gCurrentDrag	= gWorkflowObjects[i];
					gCurrentDrag.startDrag(e.clientX - offset.left, e.clientY - offset.top);
					gCurrentDrag.select();
					gCurrentSelection.push(gCurrentDrag);
					draw();
					return;
					}
			
			// look for a click on a new workflow object in the toolbar.
			for (var i = 0;i < gToolbarObjects.length;i++)
				if (gToolbarObjects[i].isMouseInside(e.clientX - offset.left, e.clientY - offset.top))
					{
					deselectAll();
					
					// create the new object (won't be added until mouse button is up)
					switch(gToolbarObjects[i].type)
						{
						case 0:	gCurrentNew	= new InitObject(gToolbarObjects[i].x, gToolbarObjects[i].y, "new", "untitled", 0);			break;
						case 1:	gCurrentNew	= new ActionObject(gToolbarObjects[i].x, gToolbarObjects[i].y, "new", "untitled", 0);		break;
						case 2:	gCurrentNew	= new DecisionObject(gToolbarObjects[i].x, gToolbarObjects[i].y, "new", "untitled", 0);	break;
						case 3:	gCurrentNew	= new StopObject(gToolbarObjects[i].x, gToolbarObjects[i].y, "new", "untitled", 0);			break;
						case 4:	gCurrentNew	= new InputObject(gToolbarObjects[i].x, gToolbarObjects[i].y, "new", "untitled", 0);			break;
						}
					gCurrentNew.startDrag(e.clientX - offset.left, e.clientY - offset.top);
					gCurrentDrag		= gCurrentNew;
					gCurrentDrag.select();
					gCurrentSelection.push(gCurrentDrag);
					gCurrentDrag.startMove();
					draw();
					return;
					}
				
			// see if user is trying to start a new connector betwwen objects
			for (var i = 0;i < gWorkflowObjects.length;i++)
				if (gWorkflowObjects[i].startingNewConnector(e.clientX - offset.left, e.clientY - offset.top))
					{
					gCurNewConnector			= gWorkflowObjects[i];
					gCurNewConnector.lastX	= gCurNewConnector.x;
					gCurNewConnector.lastY	= gCurNewConnector.y;
					return;
					}
			
			// see if user is trying to move a connector point or create a new one
			for (var i = 0;i < gWorkflowObjects.length;i++)
				for (var j = 0;j < gWorkflowObjects[i].connectors.length;j++)
					{
					var	c	= gWorkflowObjects[i].connectors[j];
					var	pt	= c.mouseOnPoint(e.clientX - offset.left, e.clientY - offset.top);
					
					// existing connector point
					if (pt > -1)
						{
						deselectAll();
						c.startDrag(e.clientX - offset.left, e.clientY - offset.top);
						gCurrentDrag		= c;
						c.select(pt);
						gCurrentSelection.push(gCurrentDrag);
						draw();
						return;
						}
					else
						{
						pt	= c.pointOnLine(e.clientX - offset.left, e.clientY - offset.top);
						
						// new connector point
						if (pt > -1)
							{
							deselectAll();
							c.startDrag(e.clientX - offset.left, e.clientY - offset.top);
							gCurrentDrag		= c;
							gCurrentSelection.push(gCurrentDrag);
							return;
							}
						}
					}
			deselectAll();
			
			// see if user clicked on a workflow object label
			for (var i = 0;i < gWorkflowObjects.length;i++)
				{
				if (gWorkflowObjects[i].isMouseOnLabel(x, y))
					{
					gCurWFLabelSel	= gWorkflowObjects[i];
					return;
					}
				}
			
			// see if the user clicked on a connector label
			for (var i = 0;i < gWorkflowObjects.length;i++)
				for (var j = 0;j < gWorkflowObjects[i].connectors.length;j++)
					{
					var	c	= gWorkflowObjects[i].connectors[j];
					if (c.pointOnLabel(x, y))
						{
						gCurConLabelSel	= c;
						return;
						}
					}
			}
		
		// deselect all workflow objects
		function deselectAll()
			{
			for (var i = 0;i < gCurrentSelection.length;i++)
				gCurrentSelection[i].deselect();
			gCurrentSelection	= new Array();
			gCurrentDrag		= null;
			gCurWFLabelSel		= null;
			gCurConLabelSel	= null;
			draw();
			}
		
		function onMouseOver(e)
			{
			}
		
		function onMouseOut(e)
			{
			}
		
		function onMouseMove(e)
			{
			document.onselectstart = function(){ return true; }
			var	offset	= getOffset(e.target);
			var	x			= e.clientX - offset.left;
			var	y			= e.clientY - offset.top;
			if (gCurrentDrag != null)
				{
				if (gCurrentDrag.hasMoved())
					{
					gCurrentDrag.move(x, y);
					draw();
					}
				else if (gCurrentDrag.hasBeenDraggedFarEnough(x, y))
					gCurrentDrag.startMove();
				}
			if (gCurWFLabelSel != null)
				{
				var	pos		= gCurWFLabelSel.labelPos;
				var	r			= gCurWFLabelSel.labelRect;
				var	newPos	= pos;
				if (pos == 0 && x < r.x)
					newPos	= 7;
				else if (pos == 0 && x > r.x + r.width)
					newPos	= 1;
				else if (pos == 1 && x < r.x)
					newPos	= 0;
				else if (pos == 1 && y > r.y + r.height)
					newPos	= 2;
				else if (pos == 2 && y < r.y)
					newPos	= 1;
				else if (pos == 2 && y > r.y + r.height)
					newPos	= 3;
				else if (pos == 3 && y < r.y)
					newPos	= 2;
				else if (pos == 3 && x < r.x)
					newPos	= 4;
				else if (pos == 4 && x > r.x + r.width)
					newPos	= 3;
				else if (pos == 4 && x < r.x)
					newPos	= 5;
				else if (pos == 5 && x > r.x + r.width)
					newPos	= 4;
				else if (pos == 5 && y < r.y)
					newPos	= 6;
				else if (pos == 6 && y > r.y + r.height)
					newPos	= 5;
				else if (pos == 6 && y < r.y)
					newPos 	= 7;
				else if (pos == 7 && y > r.y + r.height)
					newPos	= 6;
				else if (pos == 7 && x > r.x + r.width)
					newPos	= 0;
				if (pos != newPos)
					{
					gCurWFLabelSel.labelPos	= newPos;
					draw();
					}
				}
			if (gCurConLabelSel != null)
				{
				var	newPos	= gCurConLabelSel.pointOnLabelSegment(x, y);
				if (newPos > -1 && newPos != gCurConLabelSel.labelPos)
					{
					gCurConLabelSel.labelPos	= newPos;
					draw();
					}
				}
			
			if (gCurNewConnector != null)
				{
				gCurNewConnector.moveNewConnector(x, y);
				if (gCurNewConnector.proposedTarget != null)
					{
					gCurNewConnector.proposedTarget.selected	= false;
					gCurNewConnector.proposedTarget				= null;
					}
				for (var i = 0;i < gWorkflowObjects.length;i++)
					if (gWorkflowObjects[i].type != 0 && gWorkflowObjects[i] != gCurNewConnector && gWorkflowObjects[i].isMouseInside(x, y))
						{
						gCurNewConnector.proposedTarget					= gWorkflowObjects[i];
						gCurNewConnector.proposedTarget.selected		= true;
						}
				
				draw();
				}
			}
		
		function onMouseUp(e)
			{
			gCurWFLabelSel		= null;
			gCurConLabelSel	= null;
			if (gCurrentDrag != null)
				gCurrentDrag.stopDrag();
			if (gCurrentNew != null)
				{
				if (gCurrentNew.lastY > gToolBarHeight)
					gWorkflowObjects[gWorkflowObjects.length]	= gCurrentNew;
				gCurrentNew		= null;
				draw();
				}
			if (gCurNewConnector != null)
				{
				if (gCurNewConnector.proposedTarget != null)
					{
					gCurNewConnector.proposedTarget.selected	= false;
					gCurNewConnector.addConnector(gCurNewConnector.proposedTarget, "untitled");
					gCurNewConnector.proposedTarget	= null;
					}
				gCurNewConnector	= null;
				draw();
				}
			gCurrentDrag	= null;
			}
		
		function onMouseDblClick(e)
			{
			}
		
		// --------------------------------------------------------------------------------
		// base class for objects
		// --------------------------------------------------------------------------------
		function WorkflowObject(x, y, id, label, labelPos)
			{
			this.id					= id;
			this.label				= label;
			this.labelPos			= labelPos;
			this.ctx					= gCtx;
			this.x					= x;
			this.y					= y;
			this.width				= gObjectWidth;
			this.height				= gObjectHeight;
			this.selected			= false;
			this.moved				= false;
			this.connectors		= new Array();
			this.proposedTarget	= null;
			
			this.draw				= drawAction;
			this.isMouseInside	= isMouseInside;
			this.startDrag			= startDrag;
			this.move				= moveObject;
			this.select				= selectObject;
			this.deselect			= deselectObject;
			this.stopDrag			= stopDragObject;
			this.hasMoved			= hasMovedObject;
			this.startMove			= startMoveObject;
			this.hasBeenDraggedFarEnough			= hasObjectBeenDraggedFarEnough;
			this.addConnector		= addConnector;
			this.drawConnectors	= drawConnectors;
			this.isDocObject		= isDocObject;
			this.drawLabel			= drawLabelObject;
			this.isMouseOnLabel	= isMouseOnLabelObject;
			this.drawConnectorNew= drawConnectorNew;
			this.startingNewConnector	= startingNewConnector;
			this.moveNewConnector		= moveNewConnector;
			}
		
		function isDocObject()
			{
			return this.id != "new";
			}
		
		function isMouseInside(x, y)
			{
			var	halfw	= this.width / 2;
			var	halfh	= this.height / 2;
			if (x >= this.x - halfw && x <= this.x + halfw && y >= this.y - halfh && y <= this.y + halfh)
				return true;
			else
				return false;
			}
		
		function isMouseOnLabelObject(x, y)
			{
			var	r	= this.labelRect;
			if (x >= r.x && x <= r.x + r.width && y >= r.y && y <= r.y + r.height)
				return true;
			else
				return false;
			}
		
		function startDrag(x, y)
			{
			this.lastX	= x;
			this.lastY	= y;
			}
		
		function stopDragObject()
			{
			this.moved	= false;
			}
		
		function hasMovedObject()
			{
			return this.moved;
			}
		
		function startMoveObject()
			{
			this.moved	= true;
			}
		
		function hasObjectBeenDraggedFarEnough(x, y)
			{
			if (Math.abs(x - this.lastX) > 5 || Math.abs(y - this.lastY) > 5)
				return true;
			else
				return false;
			}
		
		function moveObject(x, y)
			{
			if (y > gToolBarHeight)
				{
				var	deltaX	= snapToGrid(x) - this.x;
				var	deltaY	= snapToGrid(y) - this.y;
				
				// move all 
				for (var i = 0;i < gCurrentSelection.length;i++)
					if (gCurrentSelection[i].x + deltaX < 0 || gCurrentSelection[i].y + deltaY < gToolBarHeight ||
							gCurrentSelection[i].x + deltaX > gCanvas.width || gCurrentSelection[i].y + deltaY > gCanvas.height)
						return;
				for (var i = 0;i < gCurrentSelection.length;i++)
					{
					if (gCurrentSelection.length > 1)
						{
						for (var j = 0;j < gCurrentSelection[i].connectors.length;j++)
							for (var k = 0;k < gCurrentSelection[i].connectors[j].points.length;k++)
								{
								gCurrentSelection[i].connectors[j].points[k].x	+= deltaX;
								gCurrentSelection[i].connectors[j].points[k].y	+= deltaY;
								}
						}
					gCurrentSelection[i].x	+= deltaX;
					gCurrentSelection[i].y	+= deltaY;
					}
				}
			else
				{
				this.x		= x;
				this.y		= y;
				}
			this.lastX	= x;
			this.lastY	= y;
			}
		
		function selectObject()
			{
			this.selected	= true;
			}
		
		function deselectObject()
			{
			this.selected	= false;
			}
		
		function addConnector(to, label)
			{
			var	c	= new Connector(this, to, label);
			this.connectors[this.connectors.length]	= c;
			}
		
		function drawConnectors()
			{
			for (var i = 0;i < this.connectors.length;i++)
				this.connectors[i].draw();
			}
		
		function drawLabelObject()
			{
			this.ctx.fillStyle	= gLabelColor;
			this.ctx.font			= gLabelFont;
			var	x, y;
			switch (this.labelPos)
				{
				case 0:
					{
					this.ctx.textBaseline	= "bottom";
					this.ctx.textAlign		= "center";
					x								= this.x;
					y								= this.y - this.height / 2 - gLabelMargin;
					}break;
				case 1:
					{
					this.ctx.textBaseline	= "bottom";
					this.ctx.textAlign		= "left";
					x								= this.x + this.width / 2;
					y								= this.y - this.height / 2 - gLabelMargin;
					}break;
				case 2:
					{
					this.ctx.textBaseline	= "middle";
					this.ctx.textAlign		= "left";
					x								= this.x + this.width / 2 + gLabelMargin;
					y								= this.y;
					}break;
				case 3:
					{
					this.ctx.textBaseline	= "top";
					this.ctx.textAlign		= "left";
					x								= this.x + this.width / 2;
					y								= this.y + this.height / 2 + gLabelMargin;
					}break;
				case 4:
					{
					this.ctx.textBaseline	= "top";
					this.ctx.textAlign		= "center";
					x								= this.x;
					y								= this.y + this.height / 2 + gLabelMargin;
					}break;
				case 5:
					{
					this.ctx.textBaseline	= "top";
					this.ctx.textAlign		= "right";
					x								= this.x - this.width / 2;
					y								= this.y + this.height / 2 + gLabelMargin;
					}break;
				case 6:
					{
					this.ctx.textBaseline	= "middle";
					this.ctx.textAlign		= "right";
					x								= this.x - this.width / 2 - gLabelMargin;
					y								= this.y;
					}break;
				case 7:
					{
					this.ctx.textBaseline	= "bottom";
					this.ctx.textAlign		= "right";
					x								= this.x - this.width / 2;
					y								= this.y - this.height / 2 - gLabelMargin;
					}break;
				}
			this.ctx.fillText(this.label, x, y);
			
			var	height	= gLabelBoxHeight;
			var	tm			= this.ctx.measureText(this.label);
			if (this.ctx.textBaseline == "bottom")
				{
				y			-= gLabelBoxHeight;
				height	+= gLabelMargin + 1;
				}
			else if (this.ctx.textBaseline == "middle")
				y			-= gLabelBoxHeight / 2;
			else if (this.ctx.textBaseline == "top")
				{
				y			-= gLabelMargin;
				height	+= gLabelMargin;
				}
			if (this.ctx.textAlign == "right")
				x	-= tm.width;
			else if (this.ctx.textAlign == "center")
				x	-= tm.width / 2;
			if (this.labelPos == 2 || this.labelPos == 6)
				{
				y			= this.y - this.height / 2;
				height	= this.height;
				}
			this.labelRect	= {x: x, y: y, width: tm.width, height: height};
			}
		
		function drawConnectorNew()
			{
			if (this.x == this.lastX && this.y == this.lastY || this.selected == false)
				return;
			this.ctx.strokeStyle		= "#000000";
			this.ctx.fillStyle		= "#ffffff";
			this.ctx.lineWidth		= "1";
			this.ctx.beginPath();
			this.ctx.moveTo(this.x, this.y + this.height / 2);
			this.ctx.lineTo(this.x, this.y + this.height / 2 + gLabelMargin * 2);
			this.ctx.stroke();
			this.ctx.closePath();
			this.ctx.beginPath();
			this.ctx.arc(this.x, this.y + this.height / 2 + gLabelMargin * 2, 3, 0, Math.PI*2, true); 
			this.ctx.fill();
			this.ctx.stroke();
			this.ctx.closePath();
			
			if (this == gCurNewConnector)
				{
				this.ctx.strokeStyle		= "#000000";
				this.ctx.fillStyle		= "#000000";
				this.ctx.lineWidth		= "1";
				this.ctx.beginPath();
				this.ctx.moveTo(this.x, this.y);
				this.ctx.lineTo(this.lastX, this.lastY);
				this.ctx.stroke();
				this.ctx.closePath();
				
				// arrow
				this.ctx.save();
				this.ctx.translate(this.lastX, this.lastY);
				this.ctx.rotate(Math.PI - Math.atan2(this.lastX - this.x, this.lastY - this.y));
				this.ctx.fillStyle		= "#000000";
				this.ctx.beginPath();
				this.ctx.moveTo(0, 0);
				this.ctx.lineTo(gArrowSize / 2, gArrowSize);
				this.ctx.lineTo(-gArrowSize / 2, gArrowSize);
				this.ctx.lineTo(0, 0);
				this.ctx.fill();
				this.ctx.closePath();
				this.ctx.restore();
				}
			}
		
		function startingNewConnector(x, y)
			{
			if (this.selected == false)
				return;
			if (x >= this.x - 2 && x <= this.x + 2 && y >= this.y + this.height / 2 + gLabelMargin * 2 - 2 && y <= this.y + this.height / 2 + gLabelMargin * 2 + 2)
				return true;
			else
				return false;
			}
		
		function moveNewConnector(x, y)
			{
			this.lastX	= snapToGrid(x);
			this.lastY	= snapToGrid(y);
			}
		
		
		// --------------------------------------------------------------------------------
		// connector object
		// --------------------------------------------------------------------------------
		
		function Connector(from, to, label)
			{
			this.labelPos			= 0;
			this.labelVPos			= 0;
			this.label				= label;
			this.ctx					= gCtx;
			this.from				= from;
			this.to					= to;
			this.points				= new Array();
			this.selectedPoint	= -1;
			this.moved				= false;
			this.draw							= drawConnector;
			this.addPoint						= addPoint;
			this.pointOnLine					= pointOnLine;
			this.mouseOnPoint					= mouseOnPoint;
			this.move							= movePoint;
			this.select							= selectPoint;
			this.deselect						= deselectPoint;
			this.stopDrag						= stopDragPoint;
			this.hasMoved						= hasMovedPoint;
			this.startMove						= startMovePoint;
			this.startDrag						= startDragPoint;
			this.hasBeenDraggedFarEnough	= hasPointBeenDraggedFarEnough;
			this.isDocObject					= isPointObject;
			this.pointOnLabel					= pointOnLabelConnector;
			this.pointOnLabelSegment		= pointOnLabelConnectorSegment;
			}
		
		function isPointObject()
			{
			return false;
			}
		
		function drawConnector()
			{
			this.ctx.strokeStyle		= "#000000";
			this.ctx.fillStyle		= "#000000";
			this.ctx.lineWidth		= "1";
			this.ctx.beginPath();
			this.ctx.moveTo(this.from.x, this.from.y);
			for (var i = 0;i < this.points.length;i++)
				this.ctx.lineTo(this.points[i]["x"], this.points[i]["y"]);
			this.ctx.lineTo(this.to.x, this.to.y);
			this.ctx.stroke();
			this.ctx.closePath();
			
			// arrow
			this.ctx.save();
			this.ctx.translate(this.to.x, this.to.y);
			var	lastX	= this.points.length > 0 ? this.points[this.points.length - 1].x : this.from.x;
			var	lastY	= this.points.length > 0 ? this.points[this.points.length - 1].y : this.from.y;
			this.ctx.rotate(Math.PI - Math.atan2(this.to.x - lastX, this.to.y - lastY));
			
			
			this.ctx.fillStyle		= "#000000";
			this.ctx.beginPath();
			this.ctx.moveTo(0, 0 + gObjectWidth / 2);
			this.ctx.lineTo(gArrowSize / 2, gArrowSize + gObjectWidth / 2);
			this.ctx.lineTo(-gArrowSize / 2, gArrowSize + gObjectWidth / 2);
			this.ctx.lineTo(0, 0 + gObjectWidth / 2);
			this.ctx.fill();
			this.ctx.closePath();
			
			 // draw your arrow, with its origin at [0, 0]
			this.ctx.restore();
			
			for (var i = 0;i < this.points.length;i++)
				if (this.selectedPoint == i)
					{
					this.ctx.strokeStyle		= "#000000";
					this.ctx.fillStyle		= gSelectColor;
					this.ctx.lineWidth		= "1";
					this.ctx.beginPath();
					this.ctx.arc(this.points[i]["x"], this.points[i]["y"], 5, 0, Math.PI*2, true); 
					this.ctx.fill();
					this.ctx.stroke();
					this.ctx.closePath();
					}
			
			// draw the label
			this.ctx.save();
			var	lx	= this.labelPos == 0 ? this.from.x : this.points[this.labelPos - 1].x;
			var	ly	= this.labelPos == 0 ? this.from.y : this.points[this.labelPos - 1].y;
			var	mx, my;
			if (this.points.length == 0 || this.labelPos == this.points.length)
				{
				mx	= this.to.x;
				my	= this.to.y;
				}
			else
				{
				mx	= this.points[this.labelPos].x;
				my	= this.points[this.labelPos].y;
				}
			this.ctx.translate(lx, ly);
			var	d		= Math.sqrt(Math.pow(lx - mx, 2) + Math.pow(ly - my, 2) ^ 2);
			var	angle	= Math.atan2(mx - lx, my - ly) - Math.PI / 2;
			if (angle > -Math.PI / 2)
				this.ctx.rotate(-angle);
			else
				{
				this.ctx.rotate(-Math.PI - angle);
				d	= -d;
				}
			this.ctx.fillStyle		= gLabelColor;
			this.ctx.font				= gLabelFont;
			this.ctx.textBaseline	= this.labelVPos == 0 ? "bottom" : "top";
			this.ctx.textAlign		= "center";
			var	x	= d / 2;
			var	y	= (this.labelVPos == 0 ? -1 : 1) * gLabelMargin;
			this.ctx.fillText(this.label, x, y);
			this.labelWidth	= this.ctx.measureText(this.label).width;
			this.ctx.restore();
			}
		
		function addPoint(x, y)
			{
			var	which	= this.pointOnLine(x, y);
			if (which == -1)
				return;
			this.points.splice(which, 0, {x:snapToGrid(x), y:snapToGrid(y)});
			this.select(which);
			return which;
			}
		
		function movePoint(x, y)
			{
			for (var i = 0;i < this.points.length;i++)
				if (this.selectedPoint == i)
					{
					this.points[i]["x"]	= snapToGrid(x);
					this.points[i]["y"]	= snapToGrid(y);
					break;
					}	
			}
		
		function selectPoint(which)
			{
			this.selectedPoint	= which;
			}
		
		function deselectPoint()
			{
			this.selectedPoint	= -1;
			}
		
		function pointOnLine(x3, y3)
			{
			for (var i = 0;i < this.points.length + 1;i++)
				{
				var	x1	= i == 0 ? this.from.x : this.points[i - 1]["x"];
				var	y1	= i == 0 ? this.from.y : this.points[i - 1]["y"];
				var	x2	= i == this.points.length ? this.to.x : this.points[i]["x"];
				var	y2	= i == this.points.length ? this.to.y : this.points[i]["y"];
				var	d		= distanceToLineSegment(x1, y1, x2, y2, x3, y3);
				if (d < 4)
					return i;
				}
			return -1;
			}
		
		function pointOnLabelConnector(x, y)
			{
			if (this.pointOnLabelSegment(x, y) == this.labelPos)
				return true;
			else
				return false;
			}
		
		function pointOnLabelConnectorSegment(x, y)
			{
			for (var i = 0;i < this.points.length + 1;i++)
				{
				var	lx	= i == 0 ? this.from.x : this.points[i - 1].x;
				var	ly	= i == 0 ? this.from.y : this.points[i - 1].y;
				var	mx	= i == this.points.length ? this.to.x : this.points[i].x;
				var	my	= i == this.points.length ? this.to.y : this.points[i].y;
				var	cx	= (lx + mx) / 2;
				var	cy	= (ly + my) / 2;
				if (x >= cx - this.labelWidth / 2 && x <= cx + this.labelWidth / 2 && y >= cy - this.labelWidth / 2 && 
					y <= cy + this.labelWidth / 2)
					return i;
				}
			return -1;
			}
		
		function mouseOnPoint(x, y)
			{
			for (var i = 0;i < this.points.length;i++)
				{
				var	x1	= this.points[i]["x"];
				var	y1	= this.points[i]["y"];
				if (x >= x1 - 5 && x <= x1 + 5 && y >= y1 - 5 && y <= y1 + 5)
					return i;
				}
			return -1;
			}
		
		function startDragPoint(x, y)
			{
			this.lastX	= x;
			this.lastY	= y;
			}
		
		function stopDragPoint()
			{
			this.moved	= false;
			}
		
		function hasMovedPoint()
			{
			return this.moved;
			}
		
		function startMovePoint()
			{
			if (this.selectedPoint == -1)
				{
				var	which				= this.addPoint(this.lastX, this.lastY);
				this.selectedPoint	= which;
				}
			this.moved	= true;
			}
		
		function hasPointBeenDraggedFarEnough(x, y)
			{
			if (Math.abs(x - this.lastX) > 5 || Math.abs(y - this.lastY) > 5)
				return true;
			else
				return false;
			}
		
		// ----------------- init object
		
		function InitObject(x, y, id, label, labelPos)
			{
			var	wo		= new WorkflowObject(x, y, id, label, labelPos);
			wo.draw		= drawInit;
			wo.type		= 0;
			return wo;
			}
		
		function drawInit()
			{
			this.ctx.strokeStyle		= "#000000";
			this.ctx.fillStyle		= "#ffffff";
			this.ctx.lineWidth		= "2";
			this.ctx.beginPath();
			this.ctx.arc(this.x, this.y, this.width / 2, 0, Math.PI*2, true); 
			if (this.selected)
				this.ctx.fillStyle 	= gSelectColor;
			this.ctx.fill();
			this.ctx.stroke();
			this.ctx.closePath();
			this.drawLabel();
			this.drawConnectorNew();
			}
		
		// ----------------- action object
		
		function ActionObject(x, y, id, label, labelPos)
			{
			var	wo		= new WorkflowObject(x, y, id, label, labelPos);
			wo.draw		= drawAction;
			wo.type		= 1;
			return wo;
			}
		
		function drawAction()
			{
			this.ctx.strokeStyle		= "#000000";
			this.ctx.fillStyle		= "#ffffff";
			this.ctx.lineWidth		= "2";
			this.ctx.lineJoin			= "bevel";
			this.ctx.beginPath();
			var	halfw	= this.width / 2;
			var	halfh	= this.height / 2;
			this.ctx.moveTo(this.x - halfw, this.y - halfh);
			this.ctx.lineTo(this.x + halfw, this.y - halfh);
			this.ctx.lineTo(this.x + halfw, this.y + halfh);
			this.ctx.lineTo(this.x - halfw, this.y + halfh);
			this.ctx.lineTo(this.x - halfw, this.y - halfh);
			if (this.selected)
				this.ctx.fillStyle 	= gSelectColor;
			this.ctx.fill();
			this.ctx.closePath();
			this.ctx.stroke();
			this.drawLabel();
			this.drawConnectorNew();
			}
		
		// ----------------- input object
		
		function InputObject(x, y, id, label, labelPos)
			{
			var	wo		= new WorkflowObject(x, y, id, label, labelPos);
			wo.draw		= drawInput;
			wo.type		= 4;
			return wo;
			}
		
		function drawInput()
			{
			this.ctx.strokeStyle		= "#000000";
			this.ctx.fillStyle		= "#ffffff";
			this.ctx.lineWidth		= "2";
			this.ctx.lineJoin			= "bevel";
			this.ctx.beginPath();
			var	halfw	= this.width / 2;
			var	qw		= halfw / 3;
			var	halfh	= this.height / 2;
			this.ctx.moveTo(this.x - halfw, this.y - halfh);
			this.ctx.lineTo(this.x + halfw, this.y - halfh);
			this.ctx.lineTo(this.x + halfw - qw, this.y + halfh);
			this.ctx.lineTo(this.x - halfw + qw, this.y + halfh);
			this.ctx.lineTo(this.x - halfw, this.y - halfh);
			if (this.selected)
				this.ctx.fillStyle 	= gSelectColor;
			this.ctx.fill();
			this.ctx.closePath();
			this.ctx.stroke();
			this.drawLabel();
			this.drawConnectorNew();
			}
		
		// ----------------- decision object
		
		function DecisionObject(x, y, id, label, labelPos)
			{
			var	wo		= new WorkflowObject(x, y, id, label, labelPos);
			wo.draw		= drawDecision;
			wo.type		= 2;
			return wo;
			}
		
		function drawDecision()
			{
			this.ctx.strokeStyle		= "#000000";
			this.ctx.fillStyle		= "#ffffff";
			this.ctx.lineWidth		= "2";
			this.ctx.beginPath();
			var	halfw	= this.width / 2;
			var	halfh	= this.height / 2;
			this.ctx.moveTo(this.x, this.y - halfh);
			this.ctx.lineTo(this.x + halfw, this.y);
			this.ctx.lineTo(this.x, this.y + halfh);
			this.ctx.lineTo(this.x - halfw, this.y);
			this.ctx.lineTo(this.x, this.y - halfh);
			if (this.selected)
				this.ctx.fillStyle 	= gSelectColor;
			this.ctx.fill();
			this.ctx.stroke();
			this.ctx.closePath();
			this.drawLabel();
			this.drawConnectorNew();
			}
		
		// ----------------- stop object
		
		function StopObject(x, y, id, label, labelPos)
			{
			var	wo		= new WorkflowObject(x, y, id, label, labelPos);
			wo.draw		= drawStop;
			wo.type		= 3;
			wo.startingNewConnector	= rFalse;
			return wo;
			}
		
		function drawStop()
			{
			this.ctx.strokeStyle		= "#000000";
			this.ctx.fillStyle		= "#ffffff";
			this.ctx.lineWidth		= "2";
			this.ctx.beginPath();
			this.ctx.arc(this.x, this.y, this.width / 2, 0, Math.PI*2, true); 
			if (this.selected)
				this.ctx.fillStyle 	= gSelectColor;
			this.ctx.fill();
			this.ctx.stroke();
			this.ctx.closePath();
			this.ctx.beginPath();
			this.ctx.fillStyle		= "#000000";
			this.ctx.arc(this.x, this.y, this.width / 4, 0, Math.PI*2, true); 
			this.ctx.fill();
			this.ctx.closePath();
			this.drawLabel();
			}
		
		function rFalse()
			{
			return false;
			}
		
		// ----------------- high level drawing routines
		
		
		function clear()
			{
			gCtx.clearRect(0, 0, gCanvas.width, gCanvas.height);
			}
		
		function drawToolbar()
			{
			gCtx.clearRect(0, 0, gCanvas.width, gToolBarHeight);
			gCtx.strokeStyle	= "#000000";
			gCtx.lineWidth		= "2";
			gCtx.beginPath();
			gCtx.moveTo(0, gToolBarHeight);
			gCtx.lineTo(gCanvas.width, gToolBarHeight);
			gCtx.stroke();
			for (var i = 0;i < gToolbarObjects.length;i++)
				gToolbarObjects[i].draw();
			}
		
		function draw()
			{
			clear();
			for (var i = 0;i < gWorkflowObjects.length;i++)
				gWorkflowObjects[i].drawConnectors();
			for (var i = 0;i < gWorkflowObjects.length;i++)
				gWorkflowObjects[i].draw();
			drawToolbar();
			if (gCurrentNew != null)
				gCurrentNew.draw();
			}
		
		
		// ----------------- utility functions
		
		function getOffset( el )
			{
			var _x = 0;
			var _y = 0;
			while( el && !isNaN( el.offsetLeft ) && !isNaN( el.offsetTop ) )
				{
				_x += el.offsetLeft - el.scrollLeft;
				_y += el.offsetTop - el.scrollTop;
				el = el.offsetParent;
				}
			return { top: _y, left: _x };
			}
		
		function snapToGrid(v)
			{
			return Math.floor(v / gGridSize) * gGridSize + gGridSize / 2;	
			}
		
		// 1 & 2 define segment, 3 is the point to test. 
		function distanceToLineSegment(x1, y1, x2, y2, x3, y3)
			{
			var	xDelta	= x2 - x1;
			var	yDelta	= y2 - y1;
			var	u			= ((x3 - x1) * xDelta + (y3 - y1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);
			var	closest;
			if (u < 0)
				closest	= {x: x1, y: y1};
			else if (u > 1)
				closest	= {x: x2, y: y2};
			else
				closest	= {x: x1 + u * xDelta, y: y1 + u * yDelta};
			var	d		= Math.sqrt(Math.pow(x3 - closest.x, 2) + Math.pow(y3 - closest.y, 2) ^ 2);
			return d;
			}
		
		
		
		
		// ----------------- global initializations
		
		var	gToolbarObjects	= new Array();
		var	margin				= (gToolBarHeight - gObjectWidth) / 2;
		gToolbarObjects[0]		= new InitObject(margin + (gObjectWidth + margin * 2) * 0 + gObjectWidth / 2, gToolBarHeight / 2 - margin / 2, "", "Start", 4);
		gToolbarObjects[1]		= new InputObject(margin + (gObjectWidth + margin * 2) * 1 + gObjectWidth / 2, gToolBarHeight / 2 - margin / 2, "", "Input", 4);
		gToolbarObjects[2]		= new ActionObject(margin + (gObjectWidth + margin * 2) * 2 + gObjectWidth / 2, gToolBarHeight / 2 - margin / 2, "", "Action", 4);
		gToolbarObjects[3]		= new DecisionObject(margin + (gObjectWidth + margin * 2) * 3 + gObjectWidth / 2, gToolBarHeight / 2 - margin / 2, "", "Decision", 4);
		gToolbarObjects[4]		= new StopObject(margin + (gObjectWidth + margin * 2) * 4 + gObjectWidth / 2, gToolBarHeight / 2 - margin / 2, "", "Stop", 4);
		
		for (var i = 0;i < gToolbarObjects.length;i++)
			gToolbarObjects[i].drawConnectorNew	= rFalse;
		
		var	gWorkflowObjects	= new Array();
		/*for (var i = 0;i < 8;i++)
			{
			var	w	= null;
			var	x	= snapToGrid(50 + Math.floor(Math.random() * 700));
			var	y	= snapToGrid(50 + Math.floor(Math.random() * 500));
			switch (i % 4)
				{
				case 0:	w	= new InitObject(x, y, i, "untitled", i);			break;
				case 1:	w	= new ActionObject(x, y, i, "untitled", i);		break;
				case 2:	w	= new DecisionObject(x, y, i, "untitled", i);	break;
				case 3:	w	= new StopObject(x, y, i, "untitled", i);			break;
				}
			gWorkflowObjects[i]		= w;
			}
		gWorkflowObjects[0].addConnector(gWorkflowObjects[1], "workflow");*/
		draw();