package portal

trait Renderable {
	def render(scale:Float, translate : ((Float, Float)) => ((Float, Float)));
}